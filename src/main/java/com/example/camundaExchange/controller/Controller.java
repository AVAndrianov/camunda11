package com.example.camundaExchange.controller;

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

@RestController
@RequestMapping("/otc/lookup-table")
public class Controller {
    @Autowired
    private RuntimeService runtimeService;

    @GetMapping("/startProcess")
    public ResponseEntity<String> startProcessGet() {
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
