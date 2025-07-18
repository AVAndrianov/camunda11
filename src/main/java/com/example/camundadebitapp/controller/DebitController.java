package com.example.camundadebitapp.controller;

import com.example.camundadebitapp.dto.DebitRequest;
import com.example.camundadebitapp.service.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debit")
@RequiredArgsConstructor
@Slf4j
public class DebitController {
    @Autowired
    private final RuntimeService runtimeService;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private MessageSender messageSender;

    @PostMapping
    public ResponseEntity<String> startDebitProcess(@RequestBody DebitRequest request)  {
        log.info("Received debit request for account: {}, amount: {}", request.getAccountNumber(), request.getAmount());
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("accountNumber", request.getAccountNumber());
            variables.put("amount", request.getAmount());
            variables.put("transactionId", request.getTransactionId());
            System.out.println("111111111 " + request.getTransactionId());
            ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("startDebitProcess", variables);

            log.info("Camunda process started with ID: {}", processInstance.getProcessInstanceId());
            return ResponseEntity.ok("Debit process started successfully. Process Instance ID: " + processInstance.getProcessInstanceId());

        } catch (Exception e) {
            log.error("Error starting Camunda process: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Failed to start debit process: " + e.getMessage());
        }
    }

    @GetMapping("/getMessage/{orderId}")
    public boolean getMessage(@PathVariable("orderId") String orderId) {
        System.out.println("hello1111111111111111111111");
//        runtimeService.createMessageCorrelation("receiveMessageTask")
//                .processInstanceId("receiveMessageTask")
//                .correlate();
        messageSender.sendConfirmationMessage();
        return true;
    }
//
//    @GetMapping("/getMessage/{orderId}")
//    public boolean getMessage(@PathVariable("orderId") String orderId) {
//        System.out.println("hello1111111111111111111111");
////        runtimeService.createMessageCorrelation("receiveMessageTask")
////                .processInstanceId("receiveMessageTask")
////                .correlate();
//        messageSender.sendConfirmationMessage();
//        return true;
//    }
}
