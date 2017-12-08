package com.example.model.command;

import com.example.model.User;

/**
 * @author Beka Tsotsoria
 */
public class CompleteOperation {

	private User user;
	private String operationId;

	public CompleteOperation(User user, String operationId) {
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
		return "CompleteOperation{" +
				"user=" + user +
				", operationId='" + operationId + '\'' +
				'}';
	}
}
