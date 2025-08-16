package com.example.camundaExchange.configuration;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

	@Bean
	public SimpleMeterRegistry simpleMeterRegistry() {
		return new SimpleMeterRegistry();
	}

	@Bean
	public static BeanInitializationMetricsPostProcessor beanInitializationMetricsPostProcessor(SimpleMeterRegistry registry) {
		return new BeanInitializationMetricsPostProcessor(registry);
	}
}