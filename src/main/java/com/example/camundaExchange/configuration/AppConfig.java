package com.example.camundaExchange.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${download.url}")
    private String downloadUrl;

    @Bean
    public String downloadUrl() {
        return downloadUrl;
    }
}
