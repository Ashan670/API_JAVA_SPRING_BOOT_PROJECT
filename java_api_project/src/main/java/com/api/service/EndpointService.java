package com.api.service;

import org.springframework.stereotype.Service;
import com.api.model.Endpoint;
import com.DatabaseConnection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class EndpointService {

    public List<Endpoint> getAllEndpoints() {
        List<Endpoint> endpoints = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM endpoint_table");
            while (resultSet.next()) {
                Endpoint endpoint = new Endpoint();
                endpoint.setId(resultSet.getInt("id"));
                endpoint.setEndpoint(resultSet.getString("endpoint"));
                endpoints.add(endpoint);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return endpoints;
    }
    
    public void addEndpoint(Endpoint endpoint) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO endpoint_table (endpoint) VALUES (?)");
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
}
