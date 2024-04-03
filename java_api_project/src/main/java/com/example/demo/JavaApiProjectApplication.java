package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages={"com.api.controller", "com.api.service"})
public class JavaApiProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaApiProjectApplication.class, args);
    }
}
