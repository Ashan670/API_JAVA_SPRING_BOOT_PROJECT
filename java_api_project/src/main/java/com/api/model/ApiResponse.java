package com.api.model;

public class ApiResponse {
    private String status;
    private Object data;

    public ApiResponse(String status, Object data) {
        this.status = status;
        this.data = data;
    }

    // Getters and setters
}
