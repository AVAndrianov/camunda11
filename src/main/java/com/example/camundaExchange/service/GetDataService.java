package com.example.camundaExchange.service;

import com.example.camundaExchange.model.ApiResponseEntity;
import com.example.camundaExchange.repository.ApiResponseRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Сервис для получения данных из внешнего API и их сохранения в базу данных.
 */
@Service
public class GetDataService {

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
    public GetDataService(String downloadUrl, ApiResponseRepository repository) {
        this.downloadUrl = downloadUrl;
        this.repository = repository;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Загружает данные с внешнего API и сохраняет их в базу данных.
     */
    @Async
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    @Cacheable(value = "apiResponseCache", key = "'latestResponse'")
    public String fetchAndSaveData() {
        String response = null;
        try {
            response = restTemplate.getForObject(downloadUrl, String.class);
            System.out.println("Полученный JSON: ");

            ApiResponseEntity entity = new ApiResponseEntity(response);
            repository.save(entity);

            System.out.println("Данные сохранены в базу");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @CachePut(value = "apiResponseCache", key = "'latestResponse'")
    public String refreshData() {
        String response = null;
        try {
            response = restTemplate.getForObject(downloadUrl, String.class);
            ApiResponseEntity entity = new ApiResponseEntity(response);
            repository.save(entity);
            System.out.println("Кеш обновлен и данные сохранены");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Получает все сохранённые ответы из базы данных.
     *
     * @return список всех ответов API, сохранённых в базе.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public List<ApiResponseEntity> getAllResponses() {
        return repository.findAll();
    }
}
