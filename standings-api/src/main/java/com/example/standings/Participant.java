package com.example.standings;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Beka Tsotsoria
 */
public class Participant {

    private String name;
    private String operationId;
    private LocalDateTime dateTime;
    private int score;

    public Participant(String name, String operationId, LocalDateTime dateTime, int score) {
        this.name = name;
        this.operationId = operationId;
        this.dateTime = dateTime;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getOperationId() {
        return operationId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getScore() {
        return score;
    }

    public void calculateScore() {
        score = (int) getDateTime().until(LocalDateTime.now(), ChronoUnit.SECONDS);
    }

    @Override
    public String toString() {
        return "Participant{" +
            "name='" + name + '\'' +
            ", operationId='" + operationId + '\'' +
            ", dateTime=" + dateTime +
            ", score=" + score +
            '}';
    }
}
