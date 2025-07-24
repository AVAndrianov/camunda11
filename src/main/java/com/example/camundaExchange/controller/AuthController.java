package com.example.camundaExchange.controller;

import com.example.camundaExchange.dto.JwtAuthenticationResponse;
import com.example.camundaExchange.dto.SignInRequest;
import com.example.camundaExchange.dto.SignUpRequest;
import com.example.camundaExchange.dto.UserDto;
import com.example.camundaExchange.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-контроллер для обработки аутентификации и регистрации пользователей.
 * Предоставляет эндпоинты для регистрации нового пользователя и входа в систему.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IdentityService identityService;
    /**
     * Сервис аутентификации, содержащий бизнес-логику для входа и регистрации.
     */
    private final AuthenticationService authenticationService;

    /**
     * Обработка запроса на регистрацию нового пользователя.
     *
     * @param request данные для регистрации, валидированные аннотациями
     * @return объект {@link JwtAuthenticationResponse} с JWT-токеном после успешной регистрации
     */
//    @PostMapping("/sign-up")
//    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
//        return authenticationService.signUp(request);
//    }

    /**
     * Обработка запроса на вход в систему.
     *
     * @param request данные для входа, валидированные аннотациями
     * @return объект {@link JwtAuthenticationResponse} с JWT-токеном после успешного входа
     */
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }


    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserDto userDto) {
        if (identityService.createUserQuery().userId(userDto.getId()).singleResult() != null) {
            return ResponseEntity.badRequest().body("Пользователь с таким ID уже существует");
        }
        authenticationService.signUp(new SignUpRequest(userDto.getId(), userDto.getPassword()));
        User newUser = identityService.newUser(userDto.getId());
        newUser.setPassword(userDto.getPassword());
        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());

        identityService.saveUser(newUser);

        for (String role : userDto.getRoles()) {
            Group group = identityService.createGroupQuery().groupId(role).singleResult();
            if (group == null) {
                group = identityService.newGroup(role);
                identityService.saveGroup(group);
            }
            identityService.createMembership(userDto.getId(), role);
        }

        return ResponseEntity.ok("Пользователь успешно создан");
    }
}
