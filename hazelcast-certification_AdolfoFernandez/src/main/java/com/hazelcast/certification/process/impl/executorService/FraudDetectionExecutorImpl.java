package com.hazelcast.certification.process.impl.executorService;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.hazelcast.certification.domain.Result;
import com.hazelcast.certification.domain.Transaction;
import com.hazelcast.certification.process.FraudDetection;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

public class FraudDetectionExecutorImpl extends FraudDetection {

	private static final ILogger log = Logger.getLogger(FraudDetectionExecutorImpl.class);

	private HazelcastInstance hzClient;
	private ExecutorService executorService;
	private IMap<String, List<Transaction>> imdgCC;
	private BlockingQueue<Future<Result>> bufferFutures;
	
	@Override
	protected void startFraudDetection() {
		
		try {

	    	// Create the Hazelcast client
			hzClient = HazelcastClient.newHazelcastClient();

			// Get the map where the credits cards and its transactions are stored
	    	imdgCC = hzClient.getMap("imdgCC");
	        log.info("imdgCC Map adquired with current size: " + imdgCC.size());

	        // Initializes futures buffer
	        bufferFutures = new ArrayBlockingQueue<Future<Result>>(Integer.parseInt(System.getProperty("BlockingQueueSize")));
	        log.info("Future buffer initiated with size " + Integer.parseInt(System.getProperty("BlockingQueueSize")));
	        
	        // Thread that sends transactions to the Entry Processor to perform the fraud detection process 
	    	Thread putBuffer = new Thread() {
	    		public void run() {
	    			Transaction tranx;
					try {
						// ExecutorService instance obtained via Executors
				    	executorService = Executors.newFixedThreadPool(Integer.parseInt(System.getProperty("ThreadNumber")));
				        log.info("Fraud detection started with " + Integer.parseInt(System.getProperty("ThreadNumber")) + " threads");

						// Process the transactions
						while (!Thread.interrupted()) {
							while ((tranx = getNextTransaction()) != null) {
								Future<Result> future = executorService.submit(new FraudDetectionCallableTask(imdgCC, tranx));
								bufferFutures.put(future);
							}
						}
					} catch (Exception e) {
						log.severe("Error submitting fraud detection " + e.getMessage());
						e.printStackTrace();
					}
				}
			};
			putBuffer.setDaemon(true);
			putBuffer.start();
			
	        // Thread that process the results of the fraud detection process 
			Thread getBuffer = new Thread() {
	    		public void run() {
					try {
						while (!Thread.interrupted()) {
							Result tranxResult = bufferFutures.take().get();
							registerResult(tranxResult);
						}
					} catch (Exception e) {
						log.severe("Error reading fraud detection " + e.getMessage());
						e.printStackTrace();
					}
	    		}
	    	};
			getBuffer.setDaemon(true);
			getBuffer.start();

		} catch (Exception e) {
			log.severe("Error starting fraud detection " + e.getMessage());
			e.printStackTrace();
		}
	}
}
