package com.api.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.model.Endpoint;
import com.api.model.ResponseData;
import com.api.model.ResponseWithEndpoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.DatabaseConnection.DatabaseConnection;
import com.api.model.Endpoints;

@Service
public class EndpointService {

	public List<Endpoint> getAllEndpoints() {
		List<Endpoint> endpoints = new ArrayList<>();
		try {
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM endpoint_table WHERE status = 1");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Endpoint endpoint = new Endpoint();
				endpoint.setId(resultSet.getInt("id"));
				endpoint.setEndpoint(resultSet.getString("endpoint"));
				endpoints.add(endpoint);
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return endpoints;
	}

	public List<Endpoints> getAllEndpointsData() {
		List<Endpoints> endpoints = new ArrayList<>();
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT id, endpoint, status FROM endpoint_table");
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				Endpoints endpoint = new Endpoints();
				endpoint.setId(resultSet.getInt("id"));
				endpoint.setEndpoint(resultSet.getString("endpoint"));
				endpoint.setStatus(resultSet.getInt("status"));
 
				endpoints.add(endpoint);
			}
		} catch (SQLException e) {
			e.printStackTrace();  
		}
		return endpoints;
	}

	public ResponseEntity<String> getAllResponsesWithEndpointAsJson() {
		List<ResponseWithEndpoint> responseWithEndpoints = new ArrayList<>();

		try {
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection
					.prepareStatement("SELECT r.id, r.request, r.response, e.endpoint \r\n"
							+ "FROM response_table r \r\n" + "INNER JOIN endpoint_table e ON r.endpoint_id = e.id\r\n"
							+ "WHERE r.status = 1;\r\n" + "");
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String requestString = resultSet.getString("request");
				String responseString = resultSet.getString("response");
				String endpoint = resultSet.getString("endpoint");

 				ResponseData responseData = new ResponseData(id, requestString, responseString);
				Endpoint endpointObj = new Endpoint(endpoint);

				ResponseWithEndpoint responseWithEndpoint = new ResponseWithEndpoint(responseData, endpointObj);
				responseWithEndpoints.add(responseWithEndpoint);
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while processing the request.");
		}

 		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return ResponseEntity.ok(objectMapper.writeValueAsString(responseWithEndpoints));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while processing the request.");
		}
	}

	public void addEndpoint(Endpoint endpoint) {
		try {
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection
					.prepareStatement("INSERT INTO endpoint_table (endpoint, status) VALUES (?, 1)");
			statement.setString(1, endpoint.getEndpoint());
			statement.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean updateEndpoint(int id, Endpoint updatedEndpoint) {
		try {
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection
					.prepareStatement("UPDATE endpoint_table SET endpoint = ? WHERE id = ?");
			statement.setString(1, updatedEndpoint.getEndpoint());
			statement.setInt(2, id);
			int rowsUpdated = statement.executeUpdate();
			connection.close();
			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteEndpoint(int id) {
		try {
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection
					.prepareStatement("UPDATE endpoint_table SET status = 0 WHERE id = ?");
			statement.setInt(1, id);
			int rowsUpdated = statement.executeUpdate();
			connection.close();

			updateResponseStatusForEndpoint(id, 0);

			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void updateResponseStatusForEndpoint(int endpointId, int status) throws SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.getConnection();
			String sql = "UPDATE response_table SET status = ? WHERE endpoint_id = ?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, status);
			statement.setInt(2, endpointId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error occurred during response status update: " + e.getMessage());
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}

	public boolean updateEndpointStatus(String id, String status) {
		try {
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection
					.prepareStatement("UPDATE endpoint_table SET status = ? WHERE id = ?");

 			int statusValue = Integer.parseInt(status);
			int idValue = Integer.parseInt(id);

 			statement.setInt(1, statusValue);
			statement.setInt(2, idValue);

			int rowsUpdated = statement.executeUpdate();
			connection.close();
			return rowsUpdated > 0;
		} catch (SQLException | NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}

}
