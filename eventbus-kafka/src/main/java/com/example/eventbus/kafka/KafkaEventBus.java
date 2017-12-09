package com.example.eventbus.kafka;

import com.ecample.model.serde.OperationCompletedSerializer;
import com.ecample.model.serde.OperationStartedSerializer;
import com.example.model.EventBus;
import com.example.model.event.OperationCompleted;
import com.example.model.event.OperationStarted;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author Beka Tsotsoria
 */
public class KafkaEventBus implements EventBus {

    private final Logger log = LoggerFactory.getLogger(KafkaEventBus.class);

    private String address;
    private String startedTopic;
    private String completedTopic;
    private KafkaProducer<String, OperationStarted> startedProducer;
    private KafkaProducer<String, OperationCompleted> completedProducer;

    public KafkaEventBus(String address, String startedTopic, String completedTopic) {
        this.address = address;
        this.startedTopic = startedTopic;
        this.completedTopic = completedTopic;
    }

    public void open() {
        Properties props = new Properties();
        props.put("bootstrap.servers", address);
        startedProducer = new KafkaProducer<>(props, new StringSerializer(), new OperationStartedSerializer());
        completedProducer = new KafkaProducer<>(props, new StringSerializer(), new OperationCompletedSerializer());
    }

    @Override
    public void publish(OperationStarted operationStarted) {
        try {
            startedProducer.send(new ProducerRecord<>(startedTopic, operationStarted)).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Failed to publish: {}", operationStarted, e);
        }
    }

    @Override
    public void publish(OperationCompleted operationCompleted) {
        try {
            completedProducer.send(new ProducerRecord<>(completedTopic, operationCompleted)).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Failed to publish: {}", operationCompleted, e);
        }
    }

    public void close() {
        try {
            startedProducer.close();
        } finally {
            completedProducer.close();
        }
    }
}
