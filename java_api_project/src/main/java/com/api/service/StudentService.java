package com.api.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.api.model.Student;
import com.DatabaseConnection.DatabaseConnection;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM student_table");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setAge(resultSet.getInt("age"));
                student.setGrade(resultSet.getString("grade"));
                student.setEndpointId(resultSet.getInt("endpoint_id"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public List<Student> getStudentsByEndpoint(String endpoint) {
        List<Student> students = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT * FROM student_table WHERE endpoint_id = (SELECT id FROM endpoint_table WHERE endpoint = ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, endpoint);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setAge(resultSet.getInt("age"));
                student.setGrade(resultSet.getString("grade"));
                student.setEndpointId(resultSet.getInt("endpoint_id"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
   
}
