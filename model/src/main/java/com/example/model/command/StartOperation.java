package com.example.model.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Beka Tsotsoria
 */
public class StartOperation {

    private String user;
    private String operationId;

    @JsonCreator
    public StartOperation(@JsonProperty("user") String user, @JsonProperty("operationId") String operationId) {
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
        return "StartOperation{" +
            "user=" + user +
            ", operationId='" + operationId + '\'' +
            '}';
    }
}
