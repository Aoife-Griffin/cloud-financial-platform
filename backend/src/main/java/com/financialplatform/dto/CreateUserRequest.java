package com.financialplatform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank(message = "Email: ")
    @Email(message = "Invalid email format")
    String email,

    @NotBlank(message = "Password: ")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    String password
) {}
