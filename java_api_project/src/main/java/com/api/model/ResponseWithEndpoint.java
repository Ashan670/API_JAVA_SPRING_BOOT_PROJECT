package com.api.model;

public class ResponseWithEndpoint {
	private ResponseData responseData;
	private Endpoint endpoint;

	public ResponseWithEndpoint() {
	}

	public ResponseWithEndpoint(ResponseData responseData, Endpoint endpoint) {
		this.responseData = responseData;
		this.endpoint = endpoint;
	}

	public ResponseData getResponseData() {
		return responseData;
	}

	public void setResponseData(ResponseData responseData) {
		this.responseData = responseData;
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}
}
