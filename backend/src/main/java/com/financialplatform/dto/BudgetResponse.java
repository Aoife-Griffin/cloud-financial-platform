package com.financialplatform.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetResponse(
    Long id,
    Long userId,
    String name,
    BigDecimal limitAmount,
    BigDecimal spent,
    BigDecimal remaining,
    BigDecimal usagePercentage,
    String alertStatus 
) {}
