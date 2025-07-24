package com.example.camundaExchange.service;


import com.example.camundaExchange.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Сервис для работы с JWT (JSON Web Token).
 * Обеспечивает создание, валидацию и извлечение информации из токенов.
 */
@Service
public class JwtService {
    /**
     * Ключ подписи JWT, задаётся в конфигурации.
     */
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    /**
     * Извлекает имя пользователя из токена.
     *
     * @param token JWT токен
     * @return имя пользователя (subject) из токена
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Генерирует JWT токен на основе данных пользователя.
     *
     * @param userDetails объект с данными пользователя
     * @return сгенерированный JWT токен
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("email", customUserDetails.getEmail());
            claims.put("role", customUserDetails.getRole());
        }
        return generateToken(claims, userDetails);
    }

    /**
     * Проверяет валидность токена по сравнению с данными пользователя.
     *
     * @param token       JWT токен
     * @param userDetails объект с данными пользователя
     * @return true, если токен валиден и соответствует пользователю; иначе false
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Извлекает конкретное утверждение (claim) из токена.
     *
     * @param token           JWT токен
     * @param claimsResolvers функция для получения нужного утверждения из Claims
     * @param <T>             тип возвращаемого значения
     * @return значение утверждения
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Генерирует JWT токен с дополнительными утверждениями.
     *
     * @param extraClaims дополнительные данные для включения в токен
     * @param userDetails данные пользователя
     * @return сгенерированный JWT токен
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Проверяет, истёк ли срок действия токена.
     *
     * @param token JWT токен
     * @return true, если срок действия истёк; иначе false
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Извлекает дату истечения срока действия из токена.
     *
     * @param token JWT токен
     * @return дата истечения срока действия
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Извлекает все утверждения (claims) из подписанного JWT-токена.
     *
     * @param token JWT токен
     *              -  *@return Claims объекта с данными из токена
     *              -  *@throws io.jsonwebtoken.JwtException при ошибке парсинга или неподписанном/недействительном токене
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Получает секретный ключ для подписи из строки ключа.
     * -  *@return SecretKey для подписи/валидации JWT
     * -  *@throws io.jsonwebtoken.security.SecurityException при ошибке декодирования ключа
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
