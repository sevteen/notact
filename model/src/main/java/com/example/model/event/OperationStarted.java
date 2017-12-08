package com.example.model.event;

import com.example.model.User;

/**
 * @author Beka Tsotsoria
 */
public class OperationStarted {

    private User user;
    private String operationId;

    public OperationStarted(User user, String operationId) {
        this.user = user;
        this.operationId = operationId;
    }

    public User getUser() {
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
