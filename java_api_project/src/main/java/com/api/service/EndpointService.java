package com.api.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.model.Endpoint;
import com.api.model.ResponseData;
import com.api.model.ResponseWithEndpoint;
import com.DatabaseConnection.DatabaseConnection;

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

    public List<ResponseWithEndpoint> getAllResponsesWithEndpoint() {
        List<ResponseWithEndpoint> responseWithEndpoints = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT r.*, e.endpoint FROM response_table r INNER JOIN endpoint_table e ON r.endpoint_id = e.id");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ResponseData responseData = new ResponseData();
                responseData.setId(resultSet.getInt("id"));
                responseData.setData(resultSet.getString("data"));
                
                Endpoint endpoint = new Endpoint();
                endpoint.setId(resultSet.getInt("endpoint_id"));
                endpoint.setEndpoint(resultSet.getString("endpoint"));
                
                ResponseWithEndpoint responseWithEndpoint = new ResponseWithEndpoint(responseData, endpoint);
                responseWithEndpoints.add(responseWithEndpoint);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responseWithEndpoints;
    }

    public void addEndpoint(Endpoint endpoint) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO endpoint_table (endpoint, status) VALUES (?, 1)");
            statement.setString(1, endpoint.getEndpoint());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateEndpoint(int id, Endpoint updatedEndpoint) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE endpoint_table SET endpoint = ? WHERE id = ?");
            statement.setString(1, updatedEndpoint.getEndpoint());
            statement.setInt(2, id);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEndpoint(int id) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE endpoint_table SET status = 0 WHERE id = ?");
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


    public boolean updateEndpointStatus(int id, int status) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE endpoint_table SET status = ? WHERE id = ?");
            statement.setInt(1, status);
            statement.setInt(2, id);
            int rowsUpdated = statement.executeUpdate();
            connection.close();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
