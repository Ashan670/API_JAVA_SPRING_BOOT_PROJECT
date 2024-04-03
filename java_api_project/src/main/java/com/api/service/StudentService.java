package com.api.service;

import com.api.model.Student;
import com.api.model.EndpointRequest;
import com.DatabaseConnection.DatabaseConnection;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @ResponseBody
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM student_table");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Student student = mapResultSetToStudent(resultSet);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    @ResponseBody
    public List<Student> getStudentsByEndpoint(String endpoint) {
        List<Student> students = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM student_table WHERE endpoint_id = (SELECT id FROM endpoint_table WHERE endpoint = ?)")
        ) {
            statement.setString(1, endpoint);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Student student = mapResultSetToStudent(resultSet);
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    private Student mapResultSetToStudent(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getInt("id"));
        student.setName(resultSet.getString("name"));
        student.setAge(resultSet.getInt("age"));
        student.setGrade(resultSet.getString("grade"));
        student.setEndpointId(resultSet.getInt("endpoint_id"));
        return student;
    }
}
