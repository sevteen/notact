package com.example.model.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Beka Tsotsoria
 */
public class CompleteOperation {

    private String user;
    private String operationId;

    @JsonCreator
    public CompleteOperation(@JsonProperty("user") String user, @JsonProperty("operationId") String operationId) {
        this.user = user;
        this.operationId = operationId;
    }

    public String getUser() {
        return user;
    }

    public String getOperationId() {
        return operationId;
    }

    @Override
    public String toString() {
        return "CompleteOperation{" +
            "user=" + user +
            ", operationId='" + operationId + '\'' +
            '}';
    }
}
