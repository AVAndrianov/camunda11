package com.example.camundaExchange.repository;

import com.example.camundaExchange.model.ApiResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью {@link ApiResponseEntity}.
 * Предоставляет стандартные методы для выполнения CRUD-операций.
 */
public interface ApiResponseRepository extends JpaRepository<ApiResponseEntity, Long> {
}
