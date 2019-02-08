package com.hazelcast.certification.cluster;

import java.util.List;
import java.util.Map;

import com.hazelcast.certification.domain.Transaction;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

/**
 * <p>
 * Class that implements the cluster members that stores the credit cards and all of its transactions.
 *
 */
public class IMDGCertification {

	private static final ILogger log = Logger.getLogger(IMDGCertification.class);

	public static void main(String[] args) {
		
		// Start the member in the cluster
		HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance();
		log.info("Member started");

		try {
			// Get the map
			Map<String, List<Transaction>> imdgCC = hzInstance.getMap("imdgCC");
            log.info("imdgCC Map adquired with current size: " + imdgCC.size());

		} catch (Exception e) {
			log.severe("Something went wrong getting the map", e);
			hzInstance.shutdown();
		}
	}
}
