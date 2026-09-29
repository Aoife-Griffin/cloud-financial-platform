package com.financialplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionRequest(
    @NotNull(message = "Account ID is required")
    Long accountId,

    String categoryName,

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    BigDecimal amount,

    @NotBlank(message = "Transaction type is required")
    @Pattern(regexp = "^(CREDIT|DEBIT)$", message = "Transaction type must be CREDIT or DEBIT")
    String type,

    String description,

    @NotNull(message = "Transaction date is required")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    LocalDateTime transactionDate
) {}
