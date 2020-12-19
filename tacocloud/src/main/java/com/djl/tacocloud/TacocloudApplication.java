package com.djl.tacocloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//标记为springboot的应用
@SpringBootApplication
public class TacocloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(TacocloudApplication.class, args);
    }

}
