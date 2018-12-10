package com.hazelcast.certification.process.impl.executorService;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import com.hazelcast.certification.business.ruleengine.RuleEngine;
import com.hazelcast.certification.domain.Result;
import com.hazelcast.certification.domain.Transaction;
import com.hazelcast.map.EntryBackupProcessor;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

public class FraudDetectionEntryProcessor implements EntryProcessor<String, List<Transaction>>, EntryBackupProcessor<String, List<Transaction>>, DataSerializable {

	private static final long serialVersionUID = 1L;
	private static final int MIN_HIST_TRANS = 20;
	private Transaction tranx = new Transaction();
	
	public FraudDetectionEntryProcessor() {

	}

	public Result process(Entry<String, List<Transaction>> entry) {

		// Preparation of the transaction result
		Result tranxResult = new Result();
		tranxResult.setCreditCardNumber(tranx.getCreditCardNumber());
		tranxResult.setFraudTransaction(false);

		// The historical transactions for the given credit card
		List<Transaction> value = entry.getValue();

		// Check historical transactions (less than 90 days && at least 20 historical transactions)
		if (value != null) {
			
			List<Transaction> verifiedHistTraxns = FraudDetectionUtil.checkHistTranx(value);

			if (verifiedHistTraxns != null && verifiedHistTraxns.size() >= MIN_HIST_TRANS) {

				// Apply the rule engine to the transaction
				RuleEngine myRE = new RuleEngine(tranx, verifiedHistTraxns);
				myRE.executeRules();

				// Register the rule engine result
				tranxResult.setFraudTransaction(myRE.isFraudTxn());
			}

			// Add the transaction to it's historical
			value.add(tranx);
			entry.setValue(value);
		}

		return tranxResult;
	}

	public EntryBackupProcessor<String, List<Transaction>> getBackupProcessor() {
		return FraudDetectionEntryProcessor.this;
	}

	public void processBackup(Entry<String, List<Transaction>> entry) {

	}

	public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
		tranx.writeData(objectDataOutput);
	}

	public void readData(ObjectDataInput objectDataInput) throws IOException {
		tranx.readData(objectDataInput);
	}
	
	public Transaction getTranx() {
		return tranx;
	}

	public void setTranx(Transaction tranx) {
		this.tranx = tranx;
	}


}
