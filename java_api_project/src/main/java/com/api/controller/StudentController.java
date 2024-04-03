package com.api.controller;

import java.util.List;

import com.api.model.Student;
import com.api.model.EndpointRequest;
import com.api.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping("/students/getByEndpoint")
    public List<Student> getStudentsByEndpoint(@RequestBody EndpointRequest request) {
        String endpoint = request.getEndpoint();
        return studentService.getStudentsByEndpoint(endpoint);
    }
    
    @GetMapping("/api/{endpoint}")
    public List<Student> getStudentsByEndpoint(@PathVariable("endpoint") String endpoint) {
        String dynamicEndpoint = "/api/" + endpoint;
        return studentService.getStudentsByEndpoint(dynamicEndpoint);
    }

}
