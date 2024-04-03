package com.api.service;

import com.DatabaseConnection.DatabaseConnection;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

@Service
public class ApiService {

    public String getResponse(String endpoint, Map<String, Object> requestBody) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String sql = "SELECT response FROM response_table WHERE endpoint_id = (SELECT id FROM endpoint_table WHERE endpoint = ?) ";

            StringBuilder whereClause = new StringBuilder();
            for (String key : requestBody.keySet()) {
                whereClause.append("request->>'$.").append(key).append("' = ? AND ");
            }
            whereClause.setLength(whereClause.length() - 5);
            sql += " AND " + whereClause.toString();

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "/api/" + endpoint);

            int index = 2;
            for (Object value : requestBody.values()) {
                statement.setObject(index++, value);
            }

            ResultSet resultSet = statement.executeQuery();

            // Process the result
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
}
