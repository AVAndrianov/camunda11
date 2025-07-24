package com.example.camundaExchange.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.camundaExchange.model.Role;
import com.example.camundaExchange.model.User;
import com.example.camundaExchange.repository.UserRepository;

/**
 * Сервис для управления пользователями.
 * Обеспечивает операции сохранения, создания, поиска пользователей,
 * а также получение текущего пользователя и изменение его роли.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    /**
     * Сохраняет пользователя в базе данных.
     *
     * @param user пользователь, которого нужно сохранить
     * @return сохранённый пользователь
     */
    public User save(User user) {
        return repository.save(user);
    }

    /**
     * Создаёт нового пользователя после проверки уникальности имени пользователя и email.
     *
     * @param user пользователь, которого нужно создать
     * @return созданный пользователь
     * @throws RuntimeException если пользователь с таким именем или email уже существует
     */
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        return save(user);
    }

    /**
     * Находит пользователя по имени пользователя.
     *
     * @param username имя пользователя
     * @return пользователь с указанным именем
     * @throws UsernameNotFoundException если пользователь не найден
     */
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    /**
     * Возвращает {@link UserDetailsService}, использующий метод {@link #getByUsername}.
     *
     * @return {@link UserDetailsService}
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получает текущего авторизованного пользователя из контекста безопасности.
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    /**
     * Назначает текущему пользователю роль администратора и сохраняет изменения.
     */
    public void getAdmin() {
        var user = getCurrentUser();
        user.setRole(Role.ROLE_ADMIN);
        save(user);
    }
}
