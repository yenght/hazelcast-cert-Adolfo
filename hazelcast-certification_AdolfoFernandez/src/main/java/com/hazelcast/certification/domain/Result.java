package com.hazelcast.certification.domain;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;

/**
 * Created by rahul on 07/08/15.
 */
public class Result implements IdentifiedDataSerializable {

    private boolean fraudTransaction;
    private String creditCardNumber;

    public Result() {}

    public void setFraudTransaction(boolean isFraudTransaction) {
        this.fraudTransaction = isFraudTransaction;
    }

    public boolean isFraudTransaction() {
        return fraudTransaction;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }


    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(creditCardNumber);
        objectDataOutput.writeBoolean(fraudTransaction);
    }

    public void readData(ObjectDataInput objectDataInput) throws IOException {
        creditCardNumber = objectDataInput.readUTF();
        fraudTransaction = objectDataInput.readBoolean();
    }
    
    @Override
    public int getFactoryId() { 
      return ResultDataSerializableFactory.FACTORY_ID;
    }
    
    @Override
    public int getId() { 
      return ResultDataSerializableFactory.RESULT_TYPE;
    }
}


