package com.api.model;

public class UpdateEndpointStatusRequest {

	private String status;

	public UpdateEndpointStatusRequest() {
	}

	public UpdateEndpointStatusRequest(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
