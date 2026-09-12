package com.financialplatform.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public record TransactionResponse(
    Long id,
    Long accountId,
    String categoryName,
    BigDecimal amount,
    String type,
    String description,
    LocalDateTime transactionDate,
    ZonedDateTime createdAt
) {}
