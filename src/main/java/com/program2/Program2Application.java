package com.program2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.program2.service.SalarySchedule;

@SpringBootApplication
public class Program2Application {

	public static void main(String[] args) {
		SpringApplication.run(Program2Application.class, args);
		SalarySchedule a = new SalarySchedule();
		a.startScheduleTask();
	}
}
