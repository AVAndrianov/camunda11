package com.example.camundaExchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для запроса на регистрацию нового пользователя.
 * Содержит поля для имени пользователя, электронной почты и пароля.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    /**
     * Имя пользователя.
     * Обязательное поле. Длина должна быть от 5 до 50 символов.
     */
    private String username;

    /**
     * Пароль пользователя.
     * Необязательное поле. Максимальная длина — 255 символов.
     */
    private String password;
}
