package com.hazelcast.certification.process.impl.executorService;

import com.hazelcast.certification.domain.Transaction;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class FraudDetectionUtil {

	private static final ILogger log = Logger.getLogger(FraudDetectionImpl.class);
	
	private static final int TRANS_DAYS_OLD = 90;
	
	// Check if the historical transactions meets the requirements of age 
	public static List<Transaction> checkHistTranx(List<Transaction> historicalTraxns) {
		
		List<Transaction> result = new ArrayList<Transaction>();
		long transMaxAge = DateTime.now().minusDays(TRANS_DAYS_OLD).getMillis();
		
		try {
			
			Iterator<Transaction> histIterator = historicalTraxns.iterator();
			
			while (histIterator.hasNext()) {
				Transaction tranx = histIterator.next();

				// Check if the transaction is older than 90 days
				if (tranx.getTimeStamp() >= transMaxAge) {
					result.add(tranx);
				}
			}
		} catch (Exception e) {
			log.severe("Error checking the historical transactions for the given credit card", e);
		}
	
		return result;
	}
}
