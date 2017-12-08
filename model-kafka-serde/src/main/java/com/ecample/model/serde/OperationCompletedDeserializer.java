package com.ecample.model.serde;

import com.example.model.User;
import com.example.model.event.OperationCompleted;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author Beka Tsotsoria
 */
public class OperationCompletedDeserializer implements Deserializer<OperationCompleted> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {

	}

	@Override
	public OperationCompleted deserialize(String topic, byte[] data) {
		if (data == null) return null;
		try {
			String[] strs = new String(data, "UTF-8").split("\\|");
			return new OperationCompleted(new User(strs[1]), strs[0]);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("No UTF-8", e);
		}
	}

	@Override
	public void close() {

	}
}