package com.example.camundadebitapp.controller;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/message")
public class ReceiveTaskController {

    @Autowired
    private RuntimeService runtimeService;

    @PostMapping("/confirmation")
    public ResponseEntity<String> correlateMessage(
            @RequestBody ConfirmationRequest request) {

        try {
            // 1. Get the process instance id
            String processInstanceId = request.getProcessInstanceId();

            // 2. Validate processInstanceId (Optional, but recommended)
            if (processInstanceId == null || processInstanceId.isEmpty()) {
                return new ResponseEntity<>("Process Instance ID is required.", HttpStatus.BAD_REQUEST);
            }

            // 3. Create correlation variables (if needed)
            Map<String, Object> correlationVariables = new HashMap<>();
            correlationVariables.put("confirmationResult", request.getConfirmationResult()); // Example variable

            // 4. Correlate the message
            runtimeService.correlateMessage("confirmationMessage", processInstanceId, correlationVariables);

            return new ResponseEntity<>("Confirmation message sent successfully.", HttpStatus.OK);

        } catch (Exception e) {
            // Log the error for debugging purposes
            e.printStackTrace(); // Replace with proper logging

            return new ResponseEntity<>("Error sending confirmation message: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper class for the request body
    public static class ConfirmationRequest {
        private String processInstanceId;
        private String confirmationResult; // Example variable

        public String getProcessInstanceId() {
            return processInstanceId;
        }

        public void setProcessInstanceId(String processInstanceId) {
            this.processInstanceId = processInstanceId;
        }

        public String getConfirmationResult() {
            return confirmationResult;
        }

        public void setConfirmationResult(String confirmationResult) {
            this.confirmationResult = confirmationResult;
        }
    }
}
