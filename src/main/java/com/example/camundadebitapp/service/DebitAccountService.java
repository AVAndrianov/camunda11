package com.example.camundadebitapp.service;

import com.example.camundadebitapp.dto.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Random;

@Service("debitAccountService")
@Slf4j
public class DebitAccountService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws JsonProcessingException {
        String accountNumber = (String) execution.getVariable("accountNumber");
        BigDecimal amount = (BigDecimal) execution.getVariable("amount");
        String transactionId = (String) execution.getVariable("transactionId");


        String message = (String) execution.getVariable("message");
        ObjectMapper objectMapper = new ObjectMapper();
//        JsonElement[] elements = objectMapper.readValue(message, JsonElement[].class);
        JsonMapper.Response elements = objectMapper.readValue(message, JsonMapper.Response.class);
        elements.records.stream().filter(
                i -> {
                    if (
                            Objects.equals(i.BlockDate, "")
                                    && i.INN.startsWith("77")
                                    && !i.Name.contains("ИП")
                    ) {
                        return true;
                    }
                    return false;
                }
        ).forEach(System.out::println);


        execution.setVariable("elements", elements.records);
        execution.setVariable("count", elements.records.size());

        System.out.println(execution.getVariable("count"));
//        execution.setVariable("elements", elements);
//        execution.setVariable("count", elements.length);
        log.info("Attempting to debit account: {} with amount: {} for transaction: {}", accountNumber, amount, transactionId);

        Random random = new Random();
        if (random.nextBoolean()) {
            log.info("Successfully debited account: {} with amount: {}", accountNumber, amount);
            execution.setVariable("debitStatus", "SUCCESS");
        } else {
            String errorMessage = "Insufficient funds or technical error for transaction: " + transactionId;
            log.warn("Failed to debit account: {} with amount: {}. Reason: {}", accountNumber, amount, errorMessage);
            execution.setVariable("debitStatus", "FAILED");
            execution.setVariable("errorMessage", errorMessage);
            throw new BpmnError("debitFailed", errorMessage);
        }
    }
}
