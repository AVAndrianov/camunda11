package com.example.camundaExchange.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс, предоставляющий бины для URL-адреса загрузки данных и пути к файлу changelog Liquibase.
 * <p>
 * Этот класс загружает значения из внешних источников конфигурации (например, application.properties или application.yml)
 * и делает их доступными для других компонентов приложения через Spring IoC контейнер.
 */
@Configuration
public class AppConfig {

    /**
     * URL-адрес для загрузки данных, полученный из внешнего источника конфигурации.
     * Значение задается в файле конфигурации с ключом {@code download.url}.
     */
    @Value("${download.url}")
    private String downloadUrl;

    /**
     * Путь к файлу changelog Liquibase, полученный из внешнего источника конфигурации.
     * Значение задается в файле конфигурации с ключом {@code liquibase.changeLog}.
     */
    @Value("${liquibase.changeLog}")
    private String liquibaseChangeLog;

    /**
     * Создает и возвращает бин, представляющий URL-адрес для загрузки данных.
     *
     * @return строка с URL-адресом загрузки данных.
     */
    @Bean
    public String downloadUrl() {
        return downloadUrl;
    }

    /**
     * Создает и возвращает бин с путем к файлу changelog Liquibase.
     *
     * @return строка с путем к changelog файлу Liquibase.
     */
    @Bean
    public String liquibaseChangeLog() {
        return liquibaseChangeLog;
    }
}
