package com.unisender.responses;

import java.util.List;

public class SendEmailResponse {
	private String email;
	private String id;
	private List<SendEmailResponseError> errors;
	
	public SendEmailResponse(String email, String id, List<SendEmailResponseError> errors) {
		super();
		this.email = email;
		this.id = id;
		this.errors = errors;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
				"email='" + email + '\'' +
				", id='" + id + '\'' +
				", errors=" + errors +
				'}';
	}
}
