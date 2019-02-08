package com.hazelcast.certification.domain;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public class ResultDataSerializableFactory implements DataSerializableFactory {
    	   
  public static final int FACTORY_ID = 1;
  public static final int RESULT_TYPE = 1;

  @Override
  public IdentifiedDataSerializable create(int typeId) {
    if ( typeId == RESULT_TYPE ) { 
      return new Result();
    } else {
      return null; 
    }
  }
}