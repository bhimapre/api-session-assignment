package com.example.apiassignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ApiassignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiassignmentApplication.class, args);
	}

}
