package com.example.camundaExchange;

import com.example.camundaExchange.configuration.AppConfig;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, RestTemplateIntegrationMetricsIT.TestConfig.class})
@TestPropertySource(properties = {
        "download.url=http://localhost:8089/data",
        "liquibase.changeLog=classpath:/db/changelog/db.changelog-master.yaml",
        "spring.liquibase.enabled=false"
})
@TestMethodOrder(OrderAnnotation.class)
public class RestTemplateIntegrationMetricsIT {

    private static final Logger log = LoggerFactory.getLogger(RestTemplateIntegrationMetricsIT.class);

    private static WireMockServer wireMockServer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate pooledRestTemplate;

    @Autowired
    @Qualifier("defaultRestTemplate")
    private RestTemplate defaultRestTemplate;

    @Autowired
    @Qualifier("liquibaseChangeLog")
    private String liquibaseChangeLogBean;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8089));
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);

        byte[] payload = "x".repeat(1024).getBytes(StandardCharsets.UTF_8);
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/data"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withFixedDelay(20)
                        .withBody(payload)));
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    @Order(1)
    void measureDefaultVsPooledRestTemplateUnderLoad() throws Exception {
        String url = "http://localhost:8089/data";
        int totalRequests = 200;
        int concurrency = 50;

        // Warm-up
        for (int i = 0; i < 5; i++) {
            defaultRestTemplate.getForObject(url, String.class);
            pooledRestTemplate.getForObject(url, String.class);
        }

        Metrics defaultMetrics = runLoad(defaultRestTemplate, url, totalRequests, concurrency);
        Metrics pooledMetrics = runLoad(pooledRestTemplate, url, totalRequests, concurrency);

        log.info("Default RestTemplate -> totalMs={}, avgMs={}, p95Ms={}, okCount={}",
                defaultMetrics.totalMs, defaultMetrics.avgMs, defaultMetrics.p95Ms, defaultMetrics.okCount);
        log.info("Pooled  RestTemplate -> totalMs={}, avgMs={}, p95Ms={}, okCount={}",
                pooledMetrics.totalMs, pooledMetrics.avgMs, pooledMetrics.p95Ms, pooledMetrics.okCount);

        // Sanity checks so the test is meaningful but not flaky
        Assertions.assertEquals(totalRequests, defaultMetrics.okCount, "Default OK count");
        Assertions.assertEquals(totalRequests, pooledMetrics.okCount, "Pooled OK count");
    }

    @Test
    @Order(2)
    void measureLiquibaseChangeLogBeanRetrieval() {
        int iterations = 100_000;

        Instant start = Instant.now();
        for (int i = 0; i < iterations; i++) {
            String value = applicationContext.getBean("liquibaseChangeLog", String.class);
            if (value == null || value.isEmpty()) {
                throw new IllegalStateException("liquibaseChangeLog bean is empty");
            }
        }
        long totalMs = Duration.between(start, Instant.now()).toMillis();
        double avgNs = (Duration.between(start, Instant.now()).toNanos()) / (double) iterations;

        log.info("Liquibase bean retrieval -> iterations={}, totalMs={}, avgNsPerOp={}", iterations, totalMs, avgNs);

        Assertions.assertTrue(Objects.nonNull(liquibaseChangeLogBean));
    }

    private Metrics runLoad(RestTemplate restTemplate, String url, int totalRequests, int concurrency) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(concurrency);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(totalRequests);
        List<Long> latenciesMs = new ArrayList<>(totalRequests);
        int[] okCounter = new int[] {0};

        for (int i = 0; i < totalRequests; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await(10, TimeUnit.SECONDS);
                    long t0 = System.nanoTime();
                    String body = restTemplate.getForObject(url, String.class);
                    long t1 = System.nanoTime();
                    if (body != null) {
                        synchronized (okCounter) {
                            okCounter[0] += 1;
                        }
                    }
                    long elapsedMs = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
                    synchronized (latenciesMs) {
                        latenciesMs.add(elapsedMs);
                    }
                } catch (Exception ignored) {
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        Instant start = Instant.now();
        startLatch.countDown();
        doneLatch.await(60, TimeUnit.SECONDS);
        long totalMs = Duration.between(start, Instant.now()).toMillis();

        executorService.shutdownNow();

        double avg = latenciesMs.stream().mapToLong(Long::longValue).average().orElse(0.0);
        long p95 = percentile(latenciesMs, 95);

        return new Metrics(totalMs, avg, p95, okCounter[0]);
    }

    private long percentile(List<Long> values, int p) {
        if (values.isEmpty()) {
            return 0;
        }
        List<Long> copy = new ArrayList<>(values);
        Collections.sort(copy);
        int index = Math.min(copy.size() - 1, (int) Math.ceil(p / 100.0 * copy.size()) - 1);
        return copy.get(Math.max(index, 0));
    }

    static class Metrics {
        final long totalMs;
        final double avgMs;
        final long p95Ms;
        final int okCount;

        Metrics(long totalMs, double avgMs, long p95Ms, int okCount) {
            this.totalMs = totalMs;
            this.avgMs = avgMs;
            this.p95Ms = p95Ms;
            this.okCount = okCount;
        }
    }

    @org.springframework.context.annotation.Configuration
    static class TestConfig {
        @org.springframework.context.annotation.Bean("defaultRestTemplate")
        public RestTemplate defaultRestTemplate() {
            return new RestTemplate();
        }
    }
}