package com.example.camundaExchange.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 • Конфигурационный класс, предоставляющий бин для URL-адреса загрузки данных.
 • Этот класс загружает URL-адрес из внешнего источника конфигурации (например, application.properties)
 • и делает его доступным для других компонентов приложения через Spring IoC контейнер.
 */
@Configuration
public class AppConfig {

    /**
     * URL-адрес для загрузки данных, полученный из внешнего источника конфигурации.
     * Значение URL-адреса задается в файле application.properties или application.yml
     * с использованием ключа `download.url`.
     */
    @Value("${download.url}")
    private String downloadUrl;

    /**
     * Создает и возвращает бин для URL-адреса загрузки данных.
     *
     * @return Строка, представляющая URL-адрес для загрузки данных.
     */
    @Bean
    public String downloadUrl() {
        return downloadUrl;
    }
}
