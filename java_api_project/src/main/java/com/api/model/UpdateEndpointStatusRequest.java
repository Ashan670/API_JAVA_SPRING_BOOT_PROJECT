package com.api.model;

public class UpdateEndpointStatusRequest {

	 private int status;

	    public UpdateEndpointStatusRequest() {
	    }

	    public UpdateEndpointStatusRequest(int status) {
	        this.status = status;
	    }

	    public int getStatus() {
	        return status;
	    }

	    public void setStatus(int status) {
	        this.status = status;
	    }
}
