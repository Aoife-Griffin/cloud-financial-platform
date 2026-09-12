package com.financialplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetRequest(
    @NotBlank(message = "Budget name: ")
    String name,

    @NotNull(message = "Budget limit: ")
    BigDecimal amount,

    @NotNull(message = "Start date:")
    LocalDate startDate,

    @NotNull(message = "End date:")
    LocalDate endDate
) {}
