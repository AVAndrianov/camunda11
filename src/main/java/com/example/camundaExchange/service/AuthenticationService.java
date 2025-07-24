package com.example.camundaExchange.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.camundaExchange.dto.JwtAuthenticationResponse;
import com.example.camundaExchange.dto.SignInRequest;
import com.example.camundaExchange.dto.SignUpRequest;
import com.example.camundaExchange.model.Role;
import com.example.camundaExchange.model.User;
import com.example.camundaExchange.repository.UserRepository;

import java.util.List;

/**
 * Сервис для обработки аутентификации и регистрации пользователей.
 * Обеспечивает регистрацию новых пользователей, вход в систему и получение списка всех пользователей.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    /**
     * Сервис для работы с пользователями.
     */
    private final UserService userService;

    /**
     * Сервис для генерации JWT токенов.
     */
    private final JwtService jwtService;

    /**
     * Шифровальщик паролей.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Менеджер аутентификации Spring Security.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Репозиторий пользователей для доступа к базе данных.
     */
    private final UserRepository userRepository;

    /**
     * Регистрация нового пользователя и генерация JWT токена для него.
     *
     * @param request объект с данными регистрации (имя пользователя, email, пароль)
     * @return объект с JWT токеном
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Аутентификация существующего пользователя и получение JWT токена.
     *
     * @param request объект с данными входа (имя пользователя, пароль)
     * @return объект с JWT токеном
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Получение списка всех пользователей из базы данных.
     *
     * @return список пользователей
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
