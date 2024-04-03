package com.api.model;

public class Endpoint {

    private int id;
    private String endpoints;

    public Endpoint() {
    }

    public Endpoint(int id, String endpoint) {
        this.id = id;
        this.endpoints = endpoint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEndpoints() {
        return endpoints;
    }

    public void setEndpoint(String endpoints) {
        this.endpoints = endpoints;
    }
}
