package com.example.camundadebitapp.controller;

import com.example.camundadebitapp.dto.JsonMapper;
import com.example.camundadebitapp.service.MessageSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/otc/lookup-table")
@RequiredArgsConstructor
@Slf4j
public class DebitController2 {
    @Autowired
    private final RuntimeService runtimeService;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private MessageSender messageSender;

    @PostMapping
    public ResponseEntity<String> startDebitProcess(@RequestBody String message) {

        Map<String, Object> variables = new HashMap<>();
        variables.put("message", message);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("startDebitProcess", variables);

//        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("startDebitProcess", variables);
        return ResponseEntity.ok("Процесс запущен: " + processInstance.getProcessInstanceId());

//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            JsonMapper.Response response = objectMapper.readValue(message, JsonMapper.Response.class);
//            response.records.stream().filter(
//                    i -> {
//                        if (
//                                Objects.equals(i.BlockDate, "")
//                                        && i.Name.contains("Москва")
//                                        && !i.Name.contains("ИП")
//                        ) {
//                            return true;
//                        }
//                        return false;
//                    }
//            ).forEach(System.out::println);
//
//            ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("startDebitProcess", response);
//
//            return ResponseEntity.ok("Debit process started successfully. Process Instance ID: ");
//
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("Failed to start debit process: " + e.getMessage());
//        }
    }

    @RequestMapping(value = "/getCard")
    @GetMapping
    public ResponseEntity<String> startDebitProcessGet(String message) throws UnsupportedEncodingException, JsonProcessingException {
        String decodedJson = URLDecoder.decode(message, StandardCharsets.UTF_8.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        Object json = objectMapper.readValue(decodedJson, Object.class);
        String formattedJson = objectMapper.writeValueAsString(json);
        System.out.println("         dddddddddddd      "+formattedJson);
        Map<String, Object> variables = new HashMap<>();
        variables.put("message", formattedJson);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("startDebitProcess", variables);
        return ResponseEntity.ok("Процесс запущен: " + processInstance.getProcessInstanceId());
    }

//    @PostMapping("/parseJson")
//    public ResponseEntity<Response> parseJson(@RequestBody String json) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Response response;
//
//        try {
//            response = objectMapper.readValue(json, Response.class);
//            return ResponseEntity.ok(response);
//        } catch (JsonProcessingException e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }

    @GetMapping("/1")
    public ResponseEntity<String> getMessage(@PathVariable("message") String message) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonMapper.Response response = objectMapper.readValue(message, JsonMapper.Response.class);
            response.records.forEach(System.out::println);
            System.out.println(response);
            return ResponseEntity.ok("Debit process started successfully. Process Instance ID: ");

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to start debit process: " + e.getMessage());

        }


//        log.info("Received debit request for account: {}, amount: {}", request.getAccountNumber(), request.getAmount());
//        try {
//            Map<String, Object> variables = new HashMap<>();
//            variables.put("accountNumber", request.getAccountNumber());
//            variables.put("amount", request.getAmount());
//            variables.put("transactionId", request.getTransactionId());
//            System.out.println("111111111 " + request.getTransactionId());
//            ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("startDebitProcess", variables);
//
//            log.info("Camunda process started with ID: {}", processInstance.getProcessInstanceId());
//            return ResponseEntity.ok("Debit process started successfully. Process Instance ID: " + processInstance.getProcessInstanceId());
//
//        } catch (Exception e) {
//            log.error("Error starting Camunda process: {}", e.getMessage(), e);
//            return ResponseEntity.status(500).body("Failed to start debit process: " + e.getMessage());
//        }
    }

}
