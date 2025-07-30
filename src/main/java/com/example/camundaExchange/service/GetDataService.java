package com.example.camundaExchange.service;

import com.example.camundaExchange.model.ApiResponseEntity;
import com.example.camundaExchange.repository.ApiResponseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Сервис для получения данных из внешнего API и их сохранения в базу данных.
 */
@Service
public class GetDataService {

    private static final Logger logger = LoggerFactory.getLogger(GetDataService.class);

    /**
     * URL для загрузки данных.
     */
    private final String downloadUrl;

    /**
     * Репозиторий для сохранения и получения ответов API.
     */
    private final ApiResponseRepository repository;

    /**
     * RestTemplate для выполнения HTTP-запросов.
     */
    private final RestTemplate restTemplate;

    /**
     * Конструктор сервиса.
     *
     * @param downloadUrl URL для загрузки данных.
     * @param repository  Репозиторий для работы с базой данных.
     */
    public GetDataService(String downloadUrl, ApiResponseRepository repository, RestTemplate restTemplate) {
        this.downloadUrl = downloadUrl;
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    /**
     * Загружает данные с внешнего API и сохраняет их в базу данных.
     */
    @Cacheable(value = "apiResponseCache", key = "'latestResponse'")
    public String fetchAndSaveData() {
        logger.info("Получение данных по url");
        return fetchAndSave();
    }

    @CachePut(value = "apiResponseCache", key = "'latestResponse'")
    public String refreshData() {
        logger.info("Получение данных из кэша");
        return fetchAndSave();
    }

    private String fetchAndSave() {
        String response = null;
        try {
            response = restTemplate.getForObject(downloadUrl, String.class);
            if (response != null) {
                ApiResponseEntity entity = new ApiResponseEntity(response);
                repository.save(entity);
                logger.info("Данные успешно получены и сохранены");
            } else {
                logger.warn("Получен пустой ответ от API");
            }
        } catch (Exception e) {
            logger.error("Ошибка при получении данных", e);
        }
        return response;
    }

    /**
     * Получает все сохранённые ответы из базы данных.
     *
     * @return список всех ответов API, сохранённых в базе.
     */
    public List<ApiResponseEntity> getAllResponses() {
        return repository.findAll();
    }
}
