package com.example.camundaExchange.service;

import com.example.camundaExchange.model.ApiResponseEntity;
import com.example.camundaExchange.repository.ApiResponseRepository;
import org.springframework.stereotype.Service;
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
    public void fetchAndSaveData() {
        try {
            String response = restTemplate.getForObject(downloadUrl, String.class);
            System.out.println("Полученный JSON: " + response);

            ApiResponseEntity entity = new ApiResponseEntity(response);
            repository.save(entity);

            System.out.println("Данные сохранены в базу");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
