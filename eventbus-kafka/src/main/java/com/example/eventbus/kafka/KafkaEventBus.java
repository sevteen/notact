package com.example.eventbus.kafka;

import com.ecample.model.serde.OperationCompletedSerializer;
import com.ecample.model.serde.OperationStartedSerializer;
import com.example.model.EventBus;
import com.example.model.event.OperationCompleted;
import com.example.model.event.OperationStarted;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author Beka Tsotsoria
 */
public class KafkaEventBus implements EventBus {

	private String startedTopic;
	private String completedTopic;
	private KafkaProducer<String, OperationStarted> startedProducer;
	private KafkaProducer<String, OperationCompleted> completedProducer;

	public KafkaEventBus(String address, String startedTopic, String completedTopic) {
		this.startedTopic = startedTopic;
		this.completedTopic = completedTopic;
		Properties props = new Properties();
		props.put("bootstrap.servers", address);
		startedProducer = new KafkaProducer<>(props, new StringSerializer(), new OperationStartedSerializer());
		completedProducer = new KafkaProducer<>(props, new StringSerializer(), new OperationCompletedSerializer());
	}

	@Override
	public void publish(OperationStarted operationStarted) {
		startedProducer.send(new ProducerRecord<>(startedTopic, operationStarted));
	}

	@Override
	public void publish(OperationCompleted operationCompleted) {
		completedProducer.send(new ProducerRecord<>(completedTopic, operationCompleted));
	}
}
