package com.example.camundaExchange.service;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

/**
 * Интерфейс для обработки данных с фондовой биржи.
 */
public interface StockExchangeProcessor extends JavaDelegate {
    void execute(DelegateExecution execution) throws Exception;
}
