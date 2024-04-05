package com.api.model;

public class ResponseData {
	  private int id;
	    private String data;

	    public ResponseData() {}

	    public ResponseData(int id, String data) {
	        this.id = id;
	        this.data = data;
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
}
