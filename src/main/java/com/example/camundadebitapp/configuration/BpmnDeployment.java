package com.example.camundadebitapp.configuration;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Component
public class BpmnDeployment {

    @Autowired
    private RepositoryService repositoryService;

    @PostConstruct
    public void deployBpmn() {
        String bpmnFilePath = "processes/debit_process.bpmn";
        InputStream bpmnStream = getClass().getClassLoader().getResourceAsStream(bpmnFilePath);
        if (bpmnStream != null) {
            Deployment deployment = repositoryService.createDeployment()
                    .addInputStream("debit_process.bpmn", bpmnStream)
                    .deploy();
            System.out.println("BPMN процесс развернут: " + deployment.getId());
        } else {
            System.err.println("BPMN файл не найден: " + bpmnFilePath);
        }
    }
}
