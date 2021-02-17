package com.djl.configclienterueka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ConfigClientEruekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigClientEruekaApplication.class, args);
	}

}
