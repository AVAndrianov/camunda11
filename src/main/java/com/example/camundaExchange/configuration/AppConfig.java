package com.example.camundaExchange.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.util.Timeout;
import org.apache.hc.core5.util.TimeValue;

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

    @Bean
    public RestTemplate restTemplate() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(50);
        connectionManager.setValidateAfterInactivity(TimeValue.ofSeconds(5));

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(5))
                .setConnectionRequestTimeout(Timeout.ofSeconds(5))
                .setResponseTimeout(Timeout.ofSeconds(30))
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections()
                .evictIdleConnections(TimeValue.ofSeconds(30))
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return new RestTemplate(requestFactory);
    }
}
