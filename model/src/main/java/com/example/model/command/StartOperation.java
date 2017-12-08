package com.example.model.command;

import com.example.model.User;

/**
 * @author Beka Tsotsoria
 */
public class StartOperation {

	private User user;
	private String operationId;

	public StartOperation(User user, String operationId) {
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
		return "StartOperation{" +
				"user=" + user +
				", operationId='" + operationId + '\'' +
				'}';
	}
}
