package com.financialplatform.dto;

import java.time.ZonedDateTime;

public record UserResponse(
    Long id,
    String email,
    ZonedDateTime createdAt
    /// No password needed
) {}
