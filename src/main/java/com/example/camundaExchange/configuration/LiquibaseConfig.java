package com.example.camundaExchange.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Конфигурационный класс для настройки Liquibase.
 * <p>
 * Определяет бин {@link SpringLiquibase}, который управляет миграциями базы данных.
 */
@Configuration
public class LiquibaseConfig {

    /**
     * {@link DataSource} для подключения к базе данных.
     * <p>
     * Автоматически внедряется Spring.
     */
    @Autowired
    private DataSource dataSource;

    /**
     * Создает и настраивает бин {@link SpringLiquibase}.
     * <p>
     * Этот бин отвечает за выполнение миграций базы данных с использованием Liquibase.
     *
     * @return Сконфигурированный бин {@link SpringLiquibase}.
     */
    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.xml");
        liquibase.setShouldRun(false);
        return liquibase;
    }
}
