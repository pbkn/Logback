package com.example.logback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.logback.service.LogbackServiceImpl;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class LogbackApplication implements ApplicationRunner {

	@Autowired
	LogbackServiceImpl logbackServiceImpl;

	public static void main(String[] args) {
		SpringApplication.run(LogbackApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		try {
			logbackServiceImpl.runServiceCall();
		} catch (Exception ex) {
			log.error("Exception {} is handled", ex.getMessage());
		}
	}

}
