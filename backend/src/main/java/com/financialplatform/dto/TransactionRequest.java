package com.financialplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionRequest(
    @NotNull(message = "Account ID: ")
    Long accountId,

    String categoryName,

    @NotNull(message = "Amount: ")
    BigDecimal amount,

    @NotBlank(message = "Transaction type (CREDIT/DEBIT):")
    String type,

    String description,

    LocalDateTime transactionDate
) {}
