package com.djl.turbinedashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@SpringBootApplication
@EnableTurbine
@EnableEurekaClient
public class TurbineDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(TurbineDashboardApplication.class, args);
    }

}
