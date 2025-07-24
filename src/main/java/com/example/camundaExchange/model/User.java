package com.example.camundaExchange.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Модель пользователя, реализующая {@link UserDetails} для интеграции с Spring Security.
 * Представляет пользователя в базе данных и содержит информацию о его ролях и учетных данных.
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Имя пользователя (уникальное).
     */
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    /**
     * Хэшированный пароль пользователя.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Электронная почта пользователя (уникальная).
     */
    @Column(name = "email", unique = false, nullable = true)
    private String email;

    /**
     * Роль пользователя, определяющая его права доступа.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    /**
     * Возвращает коллекцию полномочий, основанных на роли пользователя.
     *
     * @return коллекция {@link GrantedAuthority}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Конструктор с параметрами для создания пользователя.
     *
     * @param username имя пользователя
     * @param password хэшированный пароль
     * @param email электронная почта
     * @param role роль пользователя
     */
    public User(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    /**
     * Проверяет, не истек ли срок действия учетной записи.
     *
     * @return всегда true (учетная запись не истекает)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, не заблокирована ли учетная запись.
     *
     * @return всегда true (учетная запись не заблокирована)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, не истекли ли сроки действия учетных данных.
     *
     * @return всегда true (учетные данные не истекают)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверяет, активна ли учетная запись.
     *
     * @return всегда true (учетная запись активна)
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
