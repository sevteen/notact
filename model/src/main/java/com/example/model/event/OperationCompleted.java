package com.example.model.event;

import com.example.model.User;

/**
 * @author Beka Tsotsoria
 */
public class OperationCompleted {

    private User user;
    private String operationId;

    public OperationCompleted(User user, String operationId) {
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
        return "OperationCompleted{" +
            "user=" + user +
            ", operationId='" + operationId + '\'' +
            '}';
    }
}
