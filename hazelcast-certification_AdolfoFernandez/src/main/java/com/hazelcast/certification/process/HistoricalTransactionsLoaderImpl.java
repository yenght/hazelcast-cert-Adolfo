package com.hazelcast.certification.process;

import java.util.List;

import com.hazelcast.certification.domain.Transaction;
import com.hazelcast.certification.util.Const;
import com.hazelcast.certification.util.TransactionsUtil;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

public class HistoricalTransactionsLoaderImpl implements HistoricalTransactionsLoader {

	private static final ILogger log = Logger.getLogger(HistoricalTransactionsLoaderImpl.class);

    public void loadHistoricalTransactions() {
    	
    	// HazelcastClient is instantiated
    	HazelcastInstance hzClient = HazelcastClient.newHazelcastClient();

		try {

			// Get the map where the credits cards and its transactions are stored
			IMap<String, List<Transaction>> imdgCC = hzClient.getMap("imdgCC");
            log.info("imdgCC Map adquired with current size: " + imdgCC.size());
			
	    	// Utils to generate credits cards and transactions
	    	TransactionsUtil tu = new TransactionsUtil();

	    	// Create credit cards
	    	for (int n = 0; n < Const.TOTAL_NUM_CC; n++)	{
	    		String cc = tu.generateCreditCardNumber(n);

		    	// Create transactions for a credit card
		    	List<Transaction> tranxList = tu.createAndGetCreditCardTransactions(cc, Const.NUM_TRANX);
	
				// Store credit cards and transactions in the iMap
            	imdgCC.set(cc, tranxList);

            	if ((n+1) % Const.LOG_NUM_CC == 0) {
	                float percent = (((n+1)/ (float)Const.TOTAL_NUM_CC)*100);
			    	log.info("Credit card transactions loaded: " + (n+1) + " / " + Const.TOTAL_NUM_CC + " -> " + percent + "%");
		    	}
	    	}
	    	
            log.info("imdgCC Map current size: " + imdgCC.size());

		} catch (Exception e) {
			log.severe("Error loading historical transactions", e);
		} finally {
	    	log.info("My job is DONE! Shutting down...");
			hzClient.shutdown();
		}
    }
}
