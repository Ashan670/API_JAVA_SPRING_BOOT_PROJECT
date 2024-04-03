package com.api.controller;

import com.api.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ApiController {

    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping("/api/{endpoint}")
    public String getResponse(@PathVariable("endpoint") String endpoint, @RequestBody Map<String, Object> requestBody) {
        return apiService.getResponse(endpoint, requestBody);
    }
}
