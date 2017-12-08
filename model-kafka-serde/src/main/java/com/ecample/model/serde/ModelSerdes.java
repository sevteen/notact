package com.ecample.model.serde;

import com.example.model.event.OperationCompleted;
import com.example.model.event.OperationStarted;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * @author Beka Tsotsoria
 */
public class ModelSerdes {

	public static final Serde<OperationStarted> OPERATION_STARTED = new OperationStartedSerde();
	public static final Serde<OperationCompleted> OPERATION_COMPLETED = new OperationCompletedSerde();

	public static class OperationStartedSerde extends BaseSerde<OperationStarted> {

		public OperationStartedSerde() {
			super(new OperationStartedSerializer(), new OperationStartedDeserializer());
		}
	}

	public static class OperationCompletedSerde extends BaseSerde<OperationCompleted> {

		public OperationCompletedSerde() {
			super(new OperationCompletedSerializer(), new OperationCompletedDeserializer());
		}
	}

	static class BaseSerde<T> implements Serde<T> {

		private Serializer<T> serializer;
		private Deserializer<T> deserializer;

		public BaseSerde(Serializer<T> serializer, Deserializer<T> deserializer) {
			this.serializer = serializer;
			this.deserializer = deserializer;
		}

		@Override
		public void configure(Map<String, ?> configs, boolean isKey) {
			serializer.configure(configs, isKey);
			deserializer.configure(configs, isKey);
		}

		@Override
		public void close() {
			serializer.close();
			deserializer.close();
		}

		@Override
		public Serializer<T> serializer() {
			return serializer;
		}

		@Override
		public Deserializer<T> deserializer() {
			return deserializer;
		}
	}
}
