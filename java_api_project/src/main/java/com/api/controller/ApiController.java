package com.api.controller;

import com.api.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@GetMapping("/allResponseData")
	public String getAllResponseData() {
		return apiService.getAllResponseData();
	}

	@PostMapping("/api/{endpoint}")
	public String getResponse(@PathVariable("endpoint") String endpoint, @RequestBody Map<String, Object> requestBody) {
		String fullEndpoint = "/api/" + endpoint;
		return apiService.getResponse(fullEndpoint, requestBody);
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

	@PutMapping("/updateResponse/{id}")
	public ResponseEntity<String> updateResponse(@PathVariable("id") int id,
			@RequestBody Map<String, Object> requestBody) {
		try {
			Map<String, Object> request = (Map<String, Object>) requestBody.get("request");
			Map<String, Object> response = (Map<String, Object>) requestBody.get("response");
			int endpointId = (int) requestBody.get("endpoint_id");

			apiService.updateResponse(id, request, response, endpointId);
			return ResponseEntity.status(HttpStatus.OK).body("Response updated successfully");
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
		}
	}

	@DeleteMapping("/deleteResponse/{id}")
	public ResponseEntity<String> deleteResponse(@PathVariable("id") int id) {
		try {
			apiService.deleteResponse(id);
			return ResponseEntity.ok("Response deleted successfully");
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}
}
