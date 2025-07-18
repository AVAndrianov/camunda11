package com.example.camundadebitapp;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class CamundaDebitAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CamundaDebitAppApplication.class, args);
    }

}
