package com.example.operations;

import com.example.eventbus.activemq.ActiveMQEventBus;
import com.example.eventbus.kafka.KafkaEventBus;
import com.example.model.EventBus;
import com.example.model.OperationProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

/**
 * @author Beka Tsotsoria
 */
@SpringBootApplication
public class Application {

    @Value("${broker.address}")
    private String brokerAddress;

    @Value("${topic.started}")
    private String startedTopic;

    @Value("${topic.completed}")
    private String completedTopic;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public OperationProcessor operationProcessor(EventBus eventBus) {
        return new OperationProcessor(eventBus);
    }

    @Profile("kafka")
    @Bean(initMethod = "open", destroyMethod = "close")
    public EventBus kafkaEventBus() {
        return new KafkaEventBus(brokerAddress, startedTopic, completedTopic);
    }

    @Profile("activemq")
    @Bean(initMethod = "open", destroyMethod = "close")
    public EventBus activemqEventBus() {
        return new ActiveMQEventBus(brokerAddress, startedTopic, completedTopic);
    }
}
