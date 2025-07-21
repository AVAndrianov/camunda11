package com.example.camundaExchange;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class CamundaAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CamundaAppApplication.class, args);
    }

}
