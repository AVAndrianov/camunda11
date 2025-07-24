package com.example.camundaExchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для ответа при аутентификации с использованием JWT.
 * Содержит JWT-токен, который используется для авторизации в системе.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationResponse {

    /**
     * JWT-токен, выданный после успешной аутентификации.
     */
    private String token;
}
