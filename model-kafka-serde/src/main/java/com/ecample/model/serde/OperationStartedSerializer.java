package com.ecample.model.serde;

import com.example.model.event.OperationStarted;
import org.apache.kafka.common.serialization.Serializer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author Beka Tsotsoria
 */
public class OperationStartedSerializer implements Serializer<OperationStarted> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, OperationStarted data) {
        if (data == null) return null;
        try {
            return (data.getOperationId() + "|" + data.getUser()).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("No UTF-8", e);
        }
    }

    @Override
    public void close() {

    }
}
