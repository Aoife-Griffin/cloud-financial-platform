package com.financialplatform.dto;

import java.math.BigDecimal;
import java.util.Map;

public record MonthlySpendingResponse(
    String month,
    int year,
    BigDecimal totalSpending,
    Map<String, BigDecimal> categoryBreakdown
) {}
