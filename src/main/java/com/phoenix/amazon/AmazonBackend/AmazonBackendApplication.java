package com.phoenix.amazon.AmazonBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AmazonBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(AmazonBackendApplication.class, args);
	}
}
