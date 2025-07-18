package com.example.camundadebitapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/data")
public class DataController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper; // For JSON processing

    @PostMapping("/process")
    public ResponseEntity<String> startDataProcessing(@RequestBody String jsonData) {
        try {
            // 1. Start the process
            Map<String, Object> variables = new HashMap<>();
            variables.put("jsonData", jsonData);
            runtimeService.startProcessInstanceByKey("dataProcessing", variables);
            return new ResponseEntity<>("Process started successfully.", HttpStatus.ACCEPTED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error starting process: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/records")
    public ResponseEntity<?> getRecords(@RequestParam String taskId) {
        try {
            // 1. Get the task
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

            if (task == null) {
                return new ResponseEntity<>("Task not found.", HttpStatus.NOT_FOUND);
            }

            // 2. Get the process variables
            Map<String, Object> processVariables = taskService.getVariables(task.getId());

            // 3. Get recordCount
            Integer recordCount = (Integer) processVariables.get("recordCount");

            if (recordCount == null) {
                return new ResponseEntity<>("Record count not available.", HttpStatus.NOT_FOUND);
            }

            // 4. Get jsonData
            String jsonDataString = (String) processVariables.get("jsonData");

            if (jsonDataString == null) {
                return new ResponseEntity<>("JSON data not available.", HttpStatus.NOT_FOUND);
            }

            // 5. Parse json data
            JsonNode jsonNode = objectMapper.readTree(jsonDataString);

            // 6. Extract records
            JsonNode recordsNode = jsonNode.get("records");

            if (recordsNode == null || !recordsNode.isArray()) {
                return new ResponseEntity<>("Records not found or invalid format.", HttpStatus.BAD_REQUEST);
            }

            // 7. Return the records
            return new ResponseEntity<>(recordsNode, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error retrieving records: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/completeTask")
    public ResponseEntity<String> completeTask(@RequestParam String taskId) {
        try {
            // 1. Get the task
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

            if (task == null) {
                return new ResponseEntity<>("Task not found.", HttpStatus.NOT_FOUND);
            }

            // 2. Complete the task
            taskService.complete(task.getId());

            return new ResponseEntity<>("Task completed successfully.", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error completing task: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
