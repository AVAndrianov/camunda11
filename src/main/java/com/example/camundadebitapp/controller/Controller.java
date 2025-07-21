package com.example.camundadebitapp.controller;

import com.example.camundadebitapp.util.URLDownloader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/otc/lookup-table")
public class Controller {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private URLDownloader urlDownloader;

    @GetMapping("/startProcess")
    public ResponseEntity<String> startProcessGet(String message) throws Exception {

        String decodedJson = java.net.URLDecoder.decode(message, java.nio.charset.StandardCharsets.UTF_8.name());
        Object json = objectMapper.readValue(decodedJson, Object.class);
        String formattedJson = objectMapper.writeValueAsString(json);
        Map<String, Object> variables = new HashMap<>();
        variables.put("message", formattedJson);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("startProcess", variables);
        return ResponseEntity.ok("Процесс запущен: " + processInstance.getProcessInstanceId());
    }

    @GetMapping("/startProcessUrl")
    public ResponseEntity<String> startProcessGetUrlDownloader() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("startProcess");
        return ResponseEntity.ok("Процесс запущен: " + processInstance.getProcessInstanceId());
    }

    @GetMapping("/downloadJson")
    public ResponseEntity<InputStreamResource> downloadJson(@RequestParam String jsonData,
                                                            @RequestParam String processInstanceId) {
        String filename = "data_" + processInstanceId + ".json";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonData.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(jsonData.getBytes().length)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new InputStreamResource(inputStream));
    }
}
