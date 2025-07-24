package com.example.camundaExchange.repository;


import com.example.camundaExchange.model.ApiResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiResponseRepository extends JpaRepository<ApiResponseEntity, Long> {
}
