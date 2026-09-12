package com.financialplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountRequest(
    @NotBlank(message = "Account name:")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    String name,

    @NotBlank(message = "Account type: ")
    String type,

    @NotBlank(message = "Currency: ")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
    String currency
) {}
