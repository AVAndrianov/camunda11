package com.example.camundadebitapp.service;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("messageSender")
public class MessageSender {
    private final RuntimeService runtimeService;

    public MessageSender(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public void sendConfirmationMessage() {
        runtimeService.correlateMessage("confirmationMessage");

    }
//    public void sendConfirmationMessage(String processInstanceId, String confirmationResult) {
//        HashMap<String, Object> variables = new HashMap<>();
//        variables.put("confirmationResult", confirmationResult);
//
//        runtimeService.correlateMessage("confirmationMessage", processInstanceId, variables);
//    }
}
