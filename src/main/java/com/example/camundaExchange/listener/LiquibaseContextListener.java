package com.example.camundaExchange.listener;

import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Слушатель контекста Spring, запускающий Liquibase после обновления контекста приложения.
 * <p>
 * Этот класс реализует интерфейс {@link ApplicationListener} для события {@link ContextRefreshedEvent}.
 * Он используется для отложенного запуска Liquibase после того, как контекст Spring полностью инициализирован.
 */
@Component
public class LiquibaseContextListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(LiquibaseContextListener.class);

    /**
     * Бин {@link SpringLiquibase}, управляющий миграциями базы данных.
     * <p>
     * Автоматически внедряется Spring.
     */
    @Autowired
    private SpringLiquibase liquibase;

    /**
     * Обработчик события {@link ContextRefreshedEvent}.
     * <p>
     * Этот метод вызывается после того, как контекст Spring полностью инициализирован.
     * Он запускает Liquibase для выполнения миграций базы данных.
     *
     * @param event Объект события {@link ContextRefreshedEvent}, содержащий информацию о событии.
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            liquibase.setShouldRun(true);
            liquibase.afterPropertiesSet();
            liquibase.setShouldRun(false);
        } catch (Exception e) {
            logger.error("Ошибка при выполнении миграций Liquibase: {}", e.getMessage(), e);
        }
    }
}
