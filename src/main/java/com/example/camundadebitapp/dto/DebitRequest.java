package com.example.camundadebitapp.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DebitRequest {
    private String accountNumber;
    private BigDecimal amount;
    private String transactionId;
}
