package com.example.camundaExchange.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String id;
    private String password;
    private String firstName;
    private String lastName;
    private List<String> roles;
}
