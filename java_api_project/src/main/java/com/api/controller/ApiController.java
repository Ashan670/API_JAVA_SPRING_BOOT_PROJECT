package com.api.controller;

import com.api.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
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
    
    @PostMapping("/insertResponse")
    public String insertResponse(@RequestBody Map<String, Object> requestBody) {
        try {
            int id = (int) requestBody.get("id");
            Map<String, Object> request = (Map<String, Object>) requestBody.get("request");
            Map<String, Object> response = (Map<String, Object>) requestBody.get("response");
            int endpointId = (int) requestBody.get("endpoint_id");

            apiService.insertResponse(id, request, response, endpointId);
            return "Response inserted successfully";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
