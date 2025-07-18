package com.example.camundadebitapp.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrganizationRequest {
    private String organizationName;
    private BigDecimal amount;
    private String transactionId;
}
