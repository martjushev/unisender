package com.unisender.responses;

import java.util.List;

public class SendEmailResponse {
	private String id;
	private int index;
	private String email;
	private List<SendEmailResponseError> errors;

	public SendEmailResponse(String id, int index, String email, List<SendEmailResponseError> errors) {
		this.id = id;
		this.index = index;
		this.email = email;
		this.errors = errors;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<SendEmailResponseError> getErrors() {
		return errors;
	}

	public void setErrors(List<SendEmailResponseError> errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return "SendEmailResponse{" +
				"id='" + id + '\'' +
				", index=" + index +
				", email='" + email + '\'' +
				", errors=" + errors +
				'}';
	}
}
