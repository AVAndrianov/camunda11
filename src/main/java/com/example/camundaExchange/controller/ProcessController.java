package com.example.camundaExchange.controller;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * REST-контроллер для управления процессами Camunda и предоставления доступа к данным.
 * Обеспечивает запуск процессов и скачивание JSON-данных, связанных с экземплярами процессов.
 */
@RestController
@RequestMapping("/otc/lookup-table")
public class ProcessController {
    /**
     * Сервис времени выполнения Camunda, используемый для управления экземплярами процессов.
     */
    @Autowired
    private RuntimeService runtimeService;

    /**
     * Запускает экземпляр процесса Camunda по сообщению "startProcess".
     *
     * @return {@link ResponseEntity} со строковым сообщением, содержащим ID запущенного экземпляра процесса.
     */
    @GetMapping("/startProcess")
    public ResponseEntity<String> startProcessGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> variables = new HashMap<>();
        String currentUser = auth.getName(); // имя пользователя
        variables.put("assignee", currentUser);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("startProcess", variables);
        return ResponseEntity.ok("Процесс запущен: " + processInstance.getProcessInstanceId());
    }

    /**
     * Предоставляет возможность скачивания JSON-данных, связанных с экземпляром процесса.
     * Создает файл JSON для скачивания на основе предоставленных данных и ID экземпляра процесса.
     *
     * @param jsonData          JSON-данные для скачивания (в виде строки).
     * @param processInstanceId ID экземпляра процесса, связанного с данными.
     * @return {@link ResponseEntity} с {@link InputStreamResource}, позволяющим скачивать JSON-файл.
     */
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
