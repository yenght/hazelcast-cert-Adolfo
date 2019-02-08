package com.hazelcast.certification.process.impl.executorService;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;

import com.hazelcast.certification.domain.Result;
import com.hazelcast.certification.domain.Transaction;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;

public class FraudDetectionCallableTask implements Callable<Result>, Serializable, HazelcastInstanceAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient HazelcastInstance hazelcastInstance;
	
	private FraudDetectionEntryProcessor fraudDetectionEntryProcessor;
	private IMap<String, List<Transaction>> imdgCC;
	
	public FraudDetectionCallableTask(IMap<String, List<Transaction>> imdgCC, Transaction tranx) {
		this.fraudDetectionEntryProcessor = new FraudDetectionEntryProcessor();
		this.fraudDetectionEntryProcessor.setTranx(tranx);
		this.imdgCC = imdgCC;
	}

	public HazelcastInstance getHazelcastInstance() {
		return hazelcastInstance;
	}

	@Override
	public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
	}

	@Override
	public Result call() throws Exception {
		// Execute the fraud detection rules within the EntryProcessor
		return (Result) imdgCC.executeOnKey(fraudDetectionEntryProcessor.getTranx().getCreditCardNumber(), fraudDetectionEntryProcessor);
	}
}
