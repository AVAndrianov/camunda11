package com.example.camundaExchange.dto;

import lombok.Data;

/**
 * DTO (Data Transfer Object) для запроса на вход в систему.
 * Содержит поля для имени пользователя и пароля.
 */
@Data
public class SignInRequest {

    /**
     * Имя пользователя.
     * Обязательное поле. Длина должна быть от 5 до 50 символов.
     */
    private String username;

    /**
     * Пароль пользователя.
     * Обязательное поле. Длина должна быть от 8 до 255 символов.
     */
    private String password;
}
