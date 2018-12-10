package com.hazelcast.certification.process;

import java.util.List;

import com.hazelcast.certification.domain.Transaction;
import com.hazelcast.certification.util.TransactionsUtil;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

public class HistoricalTransactionsLoaderImpl implements HistoricalTransactionsLoader {

	private static final ILogger log = Logger.getLogger(HistoricalTransactionsLoaderImpl.class);

	static final int NUM_CC = 30000000; 	// Number of credit cards to generate
	static final int NUM_TRANX = 20; 	// Number of transaction per credit card to generate

    public void loadHistoricalTransactions() {
    	
    	// HazelcastClient is instantiated
    	HazelcastInstance hzClient = HazelcastClient.newHazelcastClient();

		try {

			// Get the map where the credits cards and its transactions are stored
			IMap<String, List<Transaction>> imdgCC = hzClient.getMap("imdgCC");
			
	    	// Utils to generate credits cards and transactions
	    	TransactionsUtil tu = new TransactionsUtil();
	    	
	    	// Create credit cards
	    	for (int n = 0; n < NUM_CC; n++)	{
	    		String cc = tu.generateCreditCardNumber(n);

		    	// Create transactions for a credit card
		    	List<Transaction> tranxList = tu.createAndGetCreditCardTransactions(cc, NUM_TRANX);
	
				// Store credit cards and transactions in the map
		    	imdgCC.set(cc, tranxList);
		    	
		    	if ((n+1) % 1000 == 0) {
					float percent = (((n+1)/ (float)NUM_CC)*100);
			    	log.info("Loaded credit card transaction " + (n+1) + " / " + NUM_CC + " -> " + percent + "%");
		    	}
	    	}
		} catch (Exception e) {
			log.severe("Error loading historical transactions", e);
		} finally {
	    	log.info("My job is DONE! Shutting down...");
			hzClient.shutdown();
		}
    }
    
}
