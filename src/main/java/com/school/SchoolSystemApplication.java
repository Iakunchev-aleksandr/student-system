package com.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SchoolSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SchoolSystemApplication.class, args);
    }
}
