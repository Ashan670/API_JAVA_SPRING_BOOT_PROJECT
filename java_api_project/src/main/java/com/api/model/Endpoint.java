package com.api.model;

public class Endpoint {

    private int id;
    private String endpoint;

    public Endpoint() {
    }

    public Endpoint(int id, String endpoint) {
        this.id = id;
        this.endpoint = endpoint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
