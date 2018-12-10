package com.hazelcast.certification.process.impl.executorService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.hazelcast.certification.domain.Result;
import com.hazelcast.certification.domain.Transaction;
import com.hazelcast.certification.process.FraudDetection;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

public class FraudDetectionImpl extends FraudDetection {

	private static final ILogger log = Logger.getLogger(FraudDetectionImpl.class);
	private static final int MAX_ATTEMPTS = 10;
	private static final int ATTEMPT_TIMEOUT = 3;
	
	@Override
	protected void startFraudDetection() {
		
		int attempts = 0;

		// Get the map where the credits cards and its transactions are stored
    	HazelcastInstance hzClient = HazelcastClient.newHazelcastClient();
		IMap<String, List<Transaction>> imdgCC = hzClient.getMap("imdgCC");

		try {

			// EntryProcessor is instantiated
			FraudDetectionEntryProcessor fraudDetectionEntryProcessor = new FraudDetectionEntryProcessor();
			
			while (attempts < MAX_ATTEMPTS) {

				// If the queue is empty waits a few seconds before next attempt
				if (super.txnQueue.isEmpty()) {
					
					// Count the attempt
					attempts++;
					
					// Sleep some time
					TimeUnit.SECONDS.sleep(ATTEMPT_TIMEOUT);
				
				} else {
					
					// Reset attempts
					attempts = 0;
				
					// Get the next transaction
					Transaction tranx = super.getNextTransaction();
					tranx.setTxnCode(tranx.getTxnCode().trim());  // Fix some error with white spaces that runs into a crash for rule46
					
					// Set the transaction in the EntryProcessor
					fraudDetectionEntryProcessor.setTranx(tranx);
	
					// Execute the fraud detection rules within the EntryProcessor
					Result tranxResult = (Result) imdgCC.executeOnKey(tranx.getCreditCardNumber(), fraudDetectionEntryProcessor);
					
					// Fraud detection result is registered
					super.registerResult(tranxResult);
				}
			}

		} catch (Exception e) {
			log.severe("Error loading historical transactions", e);
		} finally {
			hzClient.shutdown();
		}
	}



}
