package com.example.standings;

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

    @Value("${application.id}")
    private String applicationId;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Profile("kafka")
    @Bean(initMethod = "start", destroyMethod = "stop")
    public StandingsProvider kafkaStandingsProvider() {
        return new KafkaStandingsProvider(brokerAddress, applicationId, startedTopic, completedTopic);
    }

    @Profile("activemq")
    @Bean(initMethod = "start", destroyMethod = "stop")
    public StandingsProvider activeMQStandingsProvider() {
        return new ActiveMQStandingsProvider(brokerAddress, startedTopic, completedTopic);
    }

}
