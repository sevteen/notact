package com.example.model.event;

import java.io.Serializable;

/**
 * @author Beka Tsotsoria
 */
public class OperationStarted implements Serializable {

    private String user;
    private String operationId;

    public OperationStarted(String user, String operationId) {
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
        return "OperationStarted{" +
            "user=" + user +
            ", operationId='" + operationId + '\'' +
            '}';
    }
}
