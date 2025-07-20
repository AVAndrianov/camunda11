package com.example.camundadebitapp.service;

import com.example.camundadebitapp.model.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Service("stockExchangeService")
@Slf4j
public class StockExchangeService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String message = (String) execution.getVariable("message");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonMapper.Response elements = objectMapper.readValue(message, JsonMapper.Response.class);
        elements.records = elements.records.stream().filter(
                i -> Objects.equals(i.BlockDate, "")
                        && i.INN.startsWith("77")
                        && !i.Name.contains("ИП")
        ).collect(Collectors.toList());
        System.out.println((long) elements.records.size());

        execution.setVariable("filterMessage", toJson(elements));
        execution.setVariable("count", elements.records.size());
    }

    public static String toJson(JsonMapper.Response response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(response);
    }
}
