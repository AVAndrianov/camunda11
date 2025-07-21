package com.example.camundaExchange.service;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component("exportJsonTaskListener")
public class ExportJsonTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        String jsonData = (String) delegateTask.getVariable("filterMessage");
        String processInstanceId = delegateTask.getProcessInstanceId();
        System.out.println(processInstanceId);
        String downloadUrl = String.format("/otc/lookup-table/downloadJson?jsonData=%s&processInstanceId=%s",
                encodeValue(jsonData), encodeValue(processInstanceId));
        delegateTask.setVariable("downloadUrl", downloadUrl);
    }

    private String encodeValue(String value) {
        try {
            return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
