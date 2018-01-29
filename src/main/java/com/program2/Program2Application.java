package com.program2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Program2Application {

	public static void main(String[] args) {
		SpringApplication.run(Program2Application.class, args);
		
	}
}
