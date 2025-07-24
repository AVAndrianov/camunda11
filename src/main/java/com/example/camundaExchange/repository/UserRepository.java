package com.example.camundaExchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.camundaExchange.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для доступа к данным пользователей.
 * Расширяет JpaRepository для стандартных операций с сущностью User.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по имени пользователя.
     *
     * @param username имя пользователя
     * @return Optional, содержащий пользователя, если найден; иначе пустой Optional
     */
    Optional<User> findByUsername(String username);

    /**
     * Проверяет, существует ли пользователь с указанным именем пользователя.
     *
     * @param username имя пользователя
     * @return true, если пользователь с таким именем существует; иначе false
     */
    boolean existsByUsername(String username);

    /**
     * Проверяет, существует ли пользователь с указанным email.
     *
     * @param email email пользователя
     * @return true, если пользователь с таким email существует; иначе false
     */
    boolean existsByEmail(String email);

    /**
     * Возвращает список всех пользователей.
     *
     * @return список пользователей
     */
    @Override
    List<User> findAll();
}
