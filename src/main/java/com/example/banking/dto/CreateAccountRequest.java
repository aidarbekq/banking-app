package com.example.banking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class CreateAccountRequest {
    @NotBlank(message = "Account number must not be blank")
    @Schema(description = "Номер счета", example = "ACC12345678")
    private String accountNumber;

    @DecimalMin(value = "0.0", inclusive = true, message = "Initial balance must be ≥ 0")
    @Schema(description = "Начальный баланс", example = "1000.00")
    private BigDecimal initialBalance;

    public CreateAccountRequest() {}
    public CreateAccountRequest(String accountNumber, BigDecimal initialBalance) {
        this.accountNumber = accountNumber;
        this.initialBalance = initialBalance;
    }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public BigDecimal getInitialBalance() { return initialBalance; }
    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}