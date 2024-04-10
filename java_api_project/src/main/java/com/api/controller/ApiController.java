package com.api.controller;

import com.api.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@CrossOrigin
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

	@GetMapping("/allResponseDataWithStatus")
	public String getAllResponseDataWithStatus() {
		return apiService.getAllResponseDataWithStatus();
	}

	@PostMapping("/{endpoint}")
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
			boolean deleted = apiService.deleteResponse(id);
			if (deleted) {
				return ResponseEntity.ok("Response deleted successfully");
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Response with ID " + id + " not found");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}

	@PutMapping("/updateResponseStatus/{id}")
	public ResponseEntity<String> updateResponseStatus(@PathVariable("id") int id,
			@RequestBody Map<String, Integer> requestBody) {
		try {
			int status = requestBody.get("status");
			boolean updated = apiService.updateResponseStatus(id, status);
			if (updated) {
				return ResponseEntity.ok("Response status updated successfully");
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Response not found or status could not be updated");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}

}
