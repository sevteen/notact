package com.example.model.event;


import java.io.Serializable;

/**
 * @author Beka Tsotsoria
 */
public class OperationCompleted implements Serializable {

    private String user;
    private String operationId;

    public OperationCompleted(String user, String operationId) {
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
        return "OperationCompleted{" +
            "user=" + user +
            ", operationId='" + operationId + '\'' +
            '}';
    }
}
