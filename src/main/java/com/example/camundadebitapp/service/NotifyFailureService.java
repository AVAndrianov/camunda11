package com.example.camundadebitapp.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service("notifyFailureService")
@Slf4j
public class NotifyFailureService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        String accountNumber = (String) execution.getVariable("accountNumber");
        String transactionId = (String) execution.getVariable("transactionId");
        String errorMessage = (String) execution.getVariable("errorMessage");
        log.error("ATTENTION: Debit operation failed for account: {} (Transaction ID: {}). Error: {}",
                accountNumber, transactionId, errorMessage);
    }
}
