package com.example.camundaExchange.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * • {@link TaskListener} реализация, отвечающая за генерацию URL-адреса для скачивания
 * • JSON-данных, связанных с экземпляром процесса Camunda. Этот слушатель вызывается
 * • при завершении задачи и устанавливает переменную процесса с именем "downloadUrl"
 * • с сгенерированным URL-адресом.
 */
@Component("exportJsonTaskListener")
public class ExportJsonTaskListener implements TaskListener {
    /**
     * Генерирует URL-адрес для скачивания, содержащий JSON-данные и ID экземпляра процесса.
     * Этот метод вызывается при завершении задачи. Он извлекает переменную "filterMessage"
     * из области видимости задачи, кодирует JSON-данные и ID экземпляра процесса,
     * и конструирует URL-адрес для скачивания JSON-данных.  Сгенерированный URL-адрес затем
     * устанавливается в качестве переменной "downloadUrl" в области видимости задачи.
     *
     * @param delegateTask {@link DelegateTask}, представляющий событие задачи. Предоставляет
     *                     доступ к переменным задачи и информации об экземпляре процесса.
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        String jsonData = (String) delegateTask.getVariable("filterMessage");
        String processInstanceId = delegateTask.getProcessInstanceId();
        System.out.println(processInstanceId);
        String downloadUrl = String.format("/otc/lookup-table/downloadJson?jsonData=%s&processInstanceId=%s",
                encodeValue(jsonData), encodeValue(processInstanceId));
        delegateTask.setVariable("downloadUrl", downloadUrl);
    }

    /**
     * Кодирует строковое значение с использованием UTF-8 URL-кодирования. Этот метод используется
     * для обеспечения правильной кодировки JSON-данных и ID экземпляра процесса для включения
     * в URL-адрес для скачивания.
     *
     * @param value Строковое значение для кодирования.
     * @return Закодированная строка.
     * @throws RuntimeException Если произошла ошибка во время URL-кодирования.
     */
    private String encodeValue(String value) {
        try {
            return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
