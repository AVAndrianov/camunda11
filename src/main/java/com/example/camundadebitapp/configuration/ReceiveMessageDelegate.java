package com.example.camundadebitapp.configuration;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ReceiveMessageDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution)  {
        String processInstanceId = execution.getProcessInstanceId();
        String activityId = execution.getCurrentActivityId();
        System.out.println("Ожидание сообщения для процесса: " + processInstanceId + ", задачи: " + activityId);
    }
}
