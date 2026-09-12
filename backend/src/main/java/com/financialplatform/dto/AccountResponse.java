package com.financialplatform.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record AccountResponse(
    Long id,
    Long userId,
    String name,
    String type,
    BigDecimal balance,
    String currency,
    ZonedDateTime createdAt
) {}
