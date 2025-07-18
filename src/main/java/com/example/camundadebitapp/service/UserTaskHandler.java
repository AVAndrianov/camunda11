package com.example.camundadebitapp.service;

import com.example.camundadebitapp.dto.JsonMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class UserTaskHandler implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Получаем массив значений из переменной процесса
        JsonMapper.Response  arrayValues = (JsonMapper.Response) execution.getVariable("elements");

        // Вычисляем количество значений
        int count = arrayValues.records.size();
        execution.setVariable("count", count);
        // Формируем список значений в строку
        String list = String.join(", ", arrayValues.records.toString());
        execution.setVariable("list", list);

        System.out.println("Количество значений: " + count);
        System.out.println("Список значений: " + list);
    }
}
