package com.example.camundaExchange.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.camundaExchange.service.JwtService;
import com.example.camundaExchange.service.UserService;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

/**
 * Фильтр для обработки JWT-токенов в каждом входящем HTTP-запросе.
 * Проверяет наличие и валидность JWT-токена в заголовке Authorization,
 * и при необходимости устанавливает аутентификацию в контекст безопасности.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Префикс Bearer в заголовке Authorization.
     */
    public static final String BEARER_PREFIX = "Bearer ";

    /**
     * Название заголовка, содержащего токен авторизации.
     */
    public static final String HEADER_NAME = "Authorization";

    /**
     * Сервис для работы с JWT-токенами.
     */
    private final JwtService jwtService;

    /**
     * Сервис пользователя, предоставляющий информацию о пользователе.
     */
    private final UserService userService;

    /**
     * Обрабатывает каждый входящий HTTP-запрос, проверяя наличие и валидность JWT-токена.
     *
     * @param request входящий HTTP-запрос
     * @param response ответ сервера
     * @param filterChain цепочка фильтров, через которую передается запрос
     * @throws ServletException при ошибках обработки фильтра
     * @throws IOException при ошибках ввода-вывода
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        var authHeader = request.getHeader(HEADER_NAME);
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        var jwt = authHeader.substring(BEARER_PREFIX.length());
        var username = jwtService.extractUserName(jwt);
        if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService
                    .userDetailsService()
                    .loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }
}
