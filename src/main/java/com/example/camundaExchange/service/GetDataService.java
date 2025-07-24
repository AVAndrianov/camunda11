package com.example.camundaExchange.service;


import com.example.camundaExchange.model.ApiResponseEntity;
import com.example.camundaExchange.repository.ApiResponseRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GetDataService {

    private final String downloadUrl;
    private final ApiResponseRepository repository;
    private final RestTemplate restTemplate;

    public GetDataService(String downloadUrl, ApiResponseRepository repository) {
        this.downloadUrl = downloadUrl;
        this.repository = repository;
        this.restTemplate = new RestTemplate();
    }

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

    public List<ApiResponseEntity> getAllResponses() {
        return repository.findAll();
    }
}
