package com.app.vdsp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class VdspApplication {

	public static void main(String[] args) {
		SpringApplication.run(VdspApplication.class, args);
	}
}
