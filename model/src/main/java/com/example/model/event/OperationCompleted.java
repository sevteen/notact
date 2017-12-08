package com.example.model.event;


/**
 * @author Beka Tsotsoria
 */
public class OperationCompleted {

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
