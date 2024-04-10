package com.api.model;

public class ResponseData {
	private int id;
	private String data;
	private String request;

	public ResponseData(int id, String data, String request) {
		super();
		this.id = id;
		this.data = data;
		this.request = request;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

}
