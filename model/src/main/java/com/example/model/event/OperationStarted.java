package com.example.model.event;

/**
 * @author Beka Tsotsoria
 */
public class OperationStarted {

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
