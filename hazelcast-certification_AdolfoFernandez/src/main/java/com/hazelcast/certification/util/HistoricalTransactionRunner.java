package com.hazelcast.certification.util;

import com.hazelcast.certification.process.HistoricalTransactionsLoader;
import com.hazelcast.certification.process.HistoricalTransactionsLoaderImpl;

public class HistoricalTransactionRunner {

	public static void main(String[] args) {

		HistoricalTransactionsLoader htl = new HistoricalTransactionsLoaderImpl();
		htl.loadHistoricalTransactions();
	}
}
