package com.example.camundaExchange.service;

import com.example.camundaExchange.model.OrganizationData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * • Сервис для получения данных с фондовой биржи, фильтрации и установки переменных процесса.
 * • Реализует интерфейс {@link JavaDelegate} для интеграции с Camunda BPM.
 */
@Service("stockExchangeService")
@Slf4j
public class StockExchangeService implements JavaDelegate {

    /**
     * URL для загрузки данных с фондовой биржи.
     */
    @Autowired
    private GetDataService getDataService;

    /**
     * Выполняет логику получения, фильтрации и обработки данных с фондовой биржи.
     * В случае ошибки активирует Timer Boundary Event и отправляет сообщение в Telegram.
     *
     * @param execution Объект {@link DelegateExecution}, содержащий информацию о текущем выполнении процесса.
     * @throws Exception В случае ошибок при загрузке или обработке данных.
     */
    @Override
    public void execute(DelegateExecution execution) {
        try {
            Integer timerCounter = (Integer) execution.getVariable("timerCounter");
            if (timerCounter == null) {
                timerCounter = 0;
            }
            timerCounter++;
            execution.setVariable("timerCounter", timerCounter);

            getDataService.fetchAndSaveData();
            String message = getDataService.getAllResponses().get(0).getJsonData();
            if (message == null || message.isEmpty()) {
                log.warn("Вернулась пустая строка.  Активируем таймер.");
                execution.setVariable("downloadSuccessful", false);
                return;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            OrganizationData.Response elements = objectMapper.readValue(message, OrganizationData.Response.class);
            elements.records = elements.records.stream().filter(
                            i -> Objects.equals(i.BlockDate, "")
                                    && i.INN.startsWith("77")
                                    && !i.Name.contains("ИП")
                    )
                    .collect(Collectors.toList());
            execution.setVariable("filterMessage", toJson(elements));
            execution.setVariable("count", elements.records.size());
            execution.setVariable("downloadSuccessful", true);

            log.info("Данные успешно загружены и обработаны.");
        } catch (JsonProcessingException e) {
            log.error("Ошибка при парсинге JSON: " + e.getMessage(), e);
            execution.setVariable("downloadSuccessful", false);
            return;
        } catch (Exception e) {
            log.error("Ошибка при загрузке данных: " + e.getMessage(), e);
            execution.setVariable("downloadSuccessful", false);
            return;
        }
    }

    /**
     * Преобразует объект {@link OrganizationData.Response} в JSON строку.
     *
     * @param response Объект {@link OrganizationData.Response} для преобразования.
     * @return JSON строка, представляющая объект.
     * @throws JsonProcessingException В случае ошибки при преобразовании в JSON.
     */
    public static String toJson(OrganizationData.Response response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(response);
    }
}
