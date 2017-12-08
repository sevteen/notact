package com.ecample.model.serde;


import com.example.model.event.OperationCompleted;
import org.apache.kafka.common.serialization.Serializer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author Beka Tsotsoria
 */
public class OperationCompletedSerializer implements Serializer<OperationCompleted> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, OperationCompleted data) {
        if (data == null) return null;
        try {
            return (data.getUser() + "|" + data.getOperationId()).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("No UTF-8", e);
        }
    }

    @Override
    public void close() {

    }
}
