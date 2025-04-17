package com.example.banking.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Информация о банковском счете")
public class AccountDto {

    @Schema(description = "Идентификатор счета", example = "100")
    private Long id;

    @Schema(description = "Номер счета", example = "ACC12345678")
    private String accountNumber;

    @Schema(description = "Баланс на счете", example = "1500.00")
    private BigDecimal balance;

    // Конструкторы
    public AccountDto() {}

    public AccountDto(Long id, String accountNumber, BigDecimal balance) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    // Getters и Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
