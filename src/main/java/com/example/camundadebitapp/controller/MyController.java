package com.example.camundadebitapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/otc/lookup-table")
public class MyController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/getCard2")
    public ResponseEntity<String> startDebitProcessGet(String message) throws Exception {
        // URL Decode
        String decodedJson = java.net.URLDecoder.decode(message, java.nio.charset.StandardCharsets.UTF_8.name());

        // Convert to JSON and Format
        Object json = objectMapper.readValue(decodedJson, Object.class);
        String formattedJson = objectMapper.writeValueAsString(json);

        // Set Variables
        Map<String, Object> variables = new HashMap<>();
        variables.put("message", formattedJson);

        // Start Process
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("startDebitProcess", variables);

        // Return
        return ResponseEntity.ok("Процесс запущен: " + processInstance.getProcessInstanceId());
    }

    @GetMapping("/downloadJson")
    public ResponseEntity<InputStreamResource> downloadJson(@RequestParam String jsonData,
                                                            @RequestParam String processInstanceId) throws IOException {
        // 1. Generate JSON File from jsonData (same logic you used before)
        String filename = "data_" + processInstanceId + ".json";

        // 2. Convert jsonData to InputStream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonData.getBytes());

        // 3. Set headers for download
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

        // 4. Return file as InputStreamResource
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(jsonData.getBytes().length)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new InputStreamResource(inputStream));
    }
}
