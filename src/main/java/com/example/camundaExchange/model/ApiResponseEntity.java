package com.example.camundaExchange.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

/**
 * Entity класс, представляющий сохранённый ответ API в базе данных.
 */
@Entity
public class ApiResponseEntity {

    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Поле для хранения JSON-данных ответа API.
     */
    @Lob
    private String jsonData;

    /**
     * Конструктор по умолчанию, необходимый для JPA.
     */
    public ApiResponseEntity() {}

    /**
     * Конструктор с параметром для инициализации jsonData.
     *
     * @param jsonData JSON-строка ответа API.
     */
    public ApiResponseEntity(String jsonData) {
        this.jsonData = jsonData;
    }

    /**
     * Возвращает уникальный идентификатор записи.
     *
     * @return id записи.
     */
    public Long getId() {
        return id;
    }

    /**
     * Возвращает сохранённые JSON-данные.
     *
     * @return строка JSON данных.
     */
    public String getJsonData() {
        return jsonData;
    }

    /**
     * Устанавливает JSON-данные.
     *
     * @param jsonData строка JSON данных для сохранения.
     */
    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
