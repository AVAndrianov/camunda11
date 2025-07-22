package com.example.camundaExchange.configuration;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

/**
 • Конфигурационный класс, выполняющий миграцию схемы базы данных Camunda при запуске приложения.
 • В частности, изменяет тип данных колонок TEXT_ в таблицах истории и времени выполнения на CLOB
 • для поддержки хранения больших объемов текстовых данных.
 */
@Configuration
public class DatabaseMigrationConfig {

    /**
     * Создает бин {@link ApplicationRunner}, который выполняет миграцию базы данных при запуске приложения.
     * Этот метод получает {@link DataSource}, выполняет SQL-запросы для изменения типа данных колонки TEXT_
     * в таблицах ACT_HI_DETAIL, ACT_RU_VARIABLE и ACT_HI_VARINST на CLOB, и выводит сообщение об успехе
     * или ошибке в консоль.
     *
     * @param dataSource {@link DataSource}, используемый для подключения к базе данных.
     * @return {@link ApplicationRunner}, выполняющий миграцию базы данных.
     */
    @Bean
    public ApplicationRunner migrateDatabase(DataSource dataSource) {
        return args -> {
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.execute("ALTER TABLE ACT_HI_DETAIL ALTER COLUMN TEXT_ SET DATA TYPE CLOB");
                stmt.execute("ALTER TABLE ACT_RU_VARIABLE ALTER COLUMN TEXT_ SET DATA TYPE CLOB");
                stmt.execute("ALTER TABLE ACT_HI_VARINST ALTER COLUMN TEXT_ SET DATA TYPE CLOB");
                System.out.println("Миграция выполнена: колонка TEXT_ теперь CLOB");
            } catch (Exception e) {
                System.err.println("Ошибка при миграции базы данных: " + e.getMessage());
            }
        };
    }
}
