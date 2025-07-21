package com.example.camundaExchange.configuration;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Configuration
public class DatabaseMigrationConfig {

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
