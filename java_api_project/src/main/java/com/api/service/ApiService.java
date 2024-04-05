package com.api.service;

import com.DatabaseConnection.DatabaseConnection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiService {

    public String getAllResponseData() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM response_table WHERE status = 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            List<Map<String, Object>> responseDataList = new ArrayList<>();

            while (resultSet.next()) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("id", resultSet.getInt("id"));
                responseData.put("request", new ObjectMapper().readValue(resultSet.getString("request"), Map.class));
                responseData.put("response", new ObjectMapper().readValue(resultSet.getString("response"), Map.class));
                responseData.put("endpoint_id", resultSet.getInt("endpoint_id"));
                responseDataList.add(responseData);
            }

            resultSet.close();
            statement.close();
            connection.close();

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(responseDataList);
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Error occurred while fetching response data!\"}";
        }
    }

    public String getResponse(String endpoint, Map<String, Object> requestBody) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            if (!isValidJson(requestBody)) {
                connection.close();
                return "{\"error\": \"Invalid request JSON format\"}";
            }

            StringBuilder whereClause = new StringBuilder();
            for (String key : requestBody.keySet()) {
                whereClause.append("AND request->>'$.").append(key).append("' = ? ");
            }

            String sql = "SELECT response FROM response_table WHERE endpoint_id = (SELECT id FROM endpoint_table WHERE endpoint = ?) "
                    + whereClause.toString();

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, endpoint);
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

    public void insertResponse(int id, Map<String, Object> request, Map<String, Object> response, int endpointId)
            throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DatabaseConnection.getConnection();

            if (getEndpointId(connection, endpointId) == -1) {
                throw new IllegalArgumentException("Endpoint does not exist in the database");
            }

            String sql = "INSERT INTO response_table (id, request, response, endpoint_id, status) VALUES (?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setObject(2, new ObjectMapper().writeValueAsString(request));
            statement.setObject(3, new ObjectMapper().writeValueAsString(response));
            statement.setInt(4, endpointId);
            statement.setInt(5, 1); 
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

    public void updateResponse(int id, Map<String, Object> request, Map<String, Object> response, int endpointId)
            throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DatabaseConnection.getConnection();

            if (getEndpointId(connection, endpointId) == -1) {
                throw new IllegalArgumentException("Endpoint does not exist in the database");
            }

            String sql = "UPDATE response_table SET request = ?, response = ?, endpoint_id = ?, status = ? WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setObject(1, new ObjectMapper().writeValueAsString(request));
            statement.setObject(2, new ObjectMapper().writeValueAsString(response));
            statement.setInt(3, endpointId);
            statement.setInt(4, 1); 
            statement.setInt(5, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error occurred during response update: " + e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public boolean deleteResponse(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean deleted = false;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE response_table SET status = 0 WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                deleted = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error occurred during response deletion: " + e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return deleted;
    }

    public boolean updateEndpointStatus(int endpointId, int status) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE response_table SET status = ? WHERE endpoint_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, status);
            statement.setInt(2, endpointId);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error occurred during endpoint status update: " + e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
    
    public boolean updateResponseStatus(int id, int status) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE response_table SET status = ? WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, status);
            statement.setInt(2, id);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
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

}
