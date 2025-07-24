package com.example.camundaExchange.configuration;

import com.example.camundaExchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Конфигурация безопасности для приложения.
 * Настраивает фильтры, правила доступа, менеджеры аутентификации и CORS.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    /**
     * Фильтр JWT-аутентификации, добавляемый в цепочку фильтров безопасности.
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Сервис пользователя, реализующий {@link org.springframework.security.core.userdetails.UserDetailsService}.
     */
    private final UserService userService;

    /**
     * Создает цепочку фильтров безопасности, настраивая правила доступа, CORS, управление сессиями и аутентификацию.
     *
     * @param http объект {@link HttpSecurity} для конфигурации безопасности
     * @return настроенный объект {@link SecurityFilterChain}
     * @throws Exception возможные исключения при конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/camunda/**").permitAll()
                        .requestMatchers("/forms/**").permitAll()
                        .requestMatchers("/engine-rest/**").permitAll()
                        .requestMatchers("/app/**", "/lib/**", "/vendor/**").permitAll()
                        .requestMatchers("/app/**", "/camunda/app/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Создает бэкенд для кодирования паролей с использованием BCrypt.
     *
     * @return реализация {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Настраивает провайдер аутентификации, использующий {@link UserService} и {@link PasswordEncoder}.
     *
     * @return реализция {@link AuthenticationProvider}
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Получает менеджер аутентификации из конфигурации Spring.
     *
     * @param config конфигурация аутентификации
     * @return реализция {@link AuthenticationManager}
     * @throws Exception возможные исключения при получении менеджера
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
