package com.example.standings;

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

    @Value("${application.id}")
    private String applicationId;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public StandingsProvider standingsProvider() {
        return new KafkaStandingsProvider(kafkaAddress, applicationId, startedTopic, completedTopic);
    }

}
