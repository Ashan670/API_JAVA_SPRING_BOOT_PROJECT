package com.api.service;

import com.DatabaseConnection.DatabaseConnection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Service
public class ApiService {

    public String getResponse(String endpoint, Map<String, Object> requestBody) {
        try {
            Connection connection = DatabaseConnection.getConnection();

             int endpointId = getEndpointId(connection, Integer.parseInt(endpoint));
            if (endpointId == -1) {
                connection.close();
                return "{\"error\": \"Endpoint does not exist in the database\"}";
            }

             if (!isValidJson(requestBody)) {
                connection.close();
                return "{\"error\": \"Invalid request JSON format\"}";
            }

             String sql = "SELECT response FROM response_table WHERE endpoint_id = ? ";
            StringBuilder whereClause = new StringBuilder();
            for (String key : requestBody.keySet()) {
                whereClause.append("AND request->>'$.").append(key).append("' = ? ");
            }
            sql += whereClause.toString();

             PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, endpointId);
            int index = 2;
            for (Object value : requestBody.values()) {
                statement.setObject(index++, value);
            }

            ResultSet resultSet = statement.executeQuery();

             if (resultSet.next()) {
                String responseData = resultSet.getString("response");
                resultSet.close();
                statement.close();
                connection.close();
                return responseData;
            } else {
                resultSet.close();
                statement.close();
                connection.close();
                return "{\"error\": \"No response found for the given endpoint and request data\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Error occurred!\"}";
        }
    }

    private int getEndpointId(Connection connection, int endpointId) throws SQLException {
        String sql = "SELECT id FROM endpoint_table WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, endpointId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            resultSet.close();
            statement.close();
            return id;
        } else {
            resultSet.close();
            statement.close();
            return -1;
        }
    }


    private boolean isValidJson(Map<String, Object> jsonMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValueAsString(jsonMap);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    public void insertResponse(int id, Map<String, Object> request, Map<String, Object> response,  int endpointId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DatabaseConnection.getConnection();

            if (getEndpointId(connection, endpointId) == -1) {
                throw new IllegalArgumentException("Endpoint does not exist in the database");
            }

             String sql = "INSERT INTO response_table (id, request, response, endpoint_id) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setObject(2, new ObjectMapper().writeValueAsString(request));
            statement.setObject(3, new ObjectMapper().writeValueAsString(response));
            statement.setInt(4, endpointId);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error occurred during response insertion: " + e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }


}
