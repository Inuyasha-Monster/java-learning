package com.djl.apiregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ApiRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiRegistryApplication.class, args);
	}

}
