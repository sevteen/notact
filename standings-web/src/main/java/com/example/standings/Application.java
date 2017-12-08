package com.example.standings;

import com.example.eventbus.kafka.KafkaEventBus;
import com.example.model.EventBus;
import com.example.model.OperationProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Beka Tsotsoria
 */
@SpringBootApplication
public class Application {

    @Value("${kafka.address}")
    private String kafkaAddress;

    @Value("${topic.started}")
    private String startedTopic;

    @Value("${topic.completed}")
    private String completedTopic;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public OperationProcessor operationProcessor() {
        return new OperationProcessor(kafkaEventBus());
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public StandingsProvider standingsProvider() {
        return new KafkaStandingsProvider(kafkaAddress, startedTopic, completedTopic);
    }

    private EventBus kafkaEventBus() {
        return new KafkaEventBus(kafkaAddress, startedTopic, completedTopic);
    }

}
