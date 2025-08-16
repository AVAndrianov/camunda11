package com.example.camundaExchange.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanInitializationMetricsPostProcessor implements BeanPostProcessor {

    private final MeterRegistry meterRegistry;
    private final Map<String, Timer.Sample> beanNameToSample = new ConcurrentHashMap<>();

    public BeanInitializationMetricsPostProcessor(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("liquibaseChangeLog".equals(beanName) || "restTemplate".equals(beanName)) {
            beanNameToSample.put(beanName, Timer.start(meterRegistry));
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Timer.Sample sample = beanNameToSample.remove(beanName);
        if (sample != null) {
            Timer timer = Timer.builder("bean.initialization")
                    .tag("bean", beanName)
                    .description("Time taken to initialize bean")
                    .register(meterRegistry);
            sample.stop(timer);
        }
        return bean;
    }
}