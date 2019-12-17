package com.greenfoxacademy.TribesBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class TribesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TribesBackendApplication.class, args);
	}

}
