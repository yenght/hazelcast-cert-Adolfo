package com.hazelcast.certification.domain;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public class TransactionDataSerializableFactory implements DataSerializableFactory {
    	   
  public static final int FACTORY_ID = 2;
  public static final int TRANX_TYPE = 2;

  @Override
  public IdentifiedDataSerializable create(int typeId) {
    if ( typeId == TRANX_TYPE ) { 
      return new Transaction();
    } else {
      return null; 
    }
  }
}