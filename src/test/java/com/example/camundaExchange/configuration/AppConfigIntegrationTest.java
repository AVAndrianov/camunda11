package com.example.camundaExchange.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.search.Search;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {AppConfig.class, TestConfig.class})
@TestPropertySource(properties = {
        "download.url=https://example.com/data",
        "liquibase.changeLog=classpath:/db/changelog/db.changelog-master.yaml"
})
class AppConfigIntegrationTest {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    @Qualifier("liquibaseChangeLog")
    private String liquibaseChangeLog;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void should_register_beans_and_record_initialization_metrics() {
        assertThat(liquibaseChangeLog).isNotBlank();
        assertThat(restTemplate).isNotNull();

        Timer liquibaseTimer = Search.in(meterRegistry)
                .name("bean.initialization")
                .tag("bean", "liquibaseChangeLog")
                .timer();
        Timer restTemplateTimer = Search.in(meterRegistry)
                .name("bean.initialization")
                .tag("bean", "restTemplate")
                .timer();

        assertThat(liquibaseTimer).as("Timer for liquibaseChangeLog bean should exist").isNotNull();
        assertThat(restTemplateTimer).as("Timer for restTemplate bean should exist").isNotNull();

        double liquibaseInitMs = liquibaseTimer.totalTime(java.util.concurrent.TimeUnit.MILLISECONDS);
        double restTemplateInitMs = restTemplateTimer.totalTime(java.util.concurrent.TimeUnit.MILLISECONDS);

        assertThat(liquibaseInitMs).isGreaterThanOrEqualTo(0.0);
        assertThat(restTemplateInitMs).isGreaterThanOrEqualTo(0.0);

        System.out.printf("Bean init times: liquibaseChangeLog=%.3f ms, restTemplate=%.3f ms%n", liquibaseInitMs, restTemplateInitMs);
    }
}