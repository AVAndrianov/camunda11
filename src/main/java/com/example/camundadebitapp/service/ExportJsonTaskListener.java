package com.example.camundadebitapp.service;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Component("exportJsonTaskListener")
public class ExportJsonTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("111111111111111111111Hello");
        // 1. Get variables
        Boolean exportJson = (Boolean) delegateTask.getVariable("exportJson");
        String jsonData = (String) delegateTask.getVariable("message");
        String processInstanceId = delegateTask.getProcessInstanceId();

        // 2. Check if export is required
        if (Boolean.TRUE.equals(exportJson)) {
            // 3. Build the URL for downloadJson endpoint
            String downloadUrl = String.format("/otc/lookup-table/downloadJson?jsonData=%s&processInstanceId=%s",
                    encodeValue(jsonData), encodeValue(processInstanceId));

            // 4. Add downloadUrl as task variable
            delegateTask.setVariable("downloadUrl", downloadUrl);
        }
    }

    private String encodeValue(String value) {
        try {
            return java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
