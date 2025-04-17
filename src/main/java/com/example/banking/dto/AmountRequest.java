package com.example.banking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public class AmountRequest {
    @DecimalMin(value = "0.01", message = "Amount must be > 0")
    @Schema(description = "Сумма операции", example = "200.00")
    private BigDecimal amount;

    public AmountRequest() {}
    public AmountRequest(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}