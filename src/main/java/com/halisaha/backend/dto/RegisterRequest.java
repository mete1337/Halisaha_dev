package com.halisaha.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank @Size(min = 6) 
        String password,
        @NotBlank @Email(message = "Invalid email format")
        String email,
        @NotBlank @Pattern(regexp = "^05\\d{9}$", message = "Phone number must be in format 05XXXXXXXXX")
        String phoneNumber,
        @NotBlank String name,
        @NotBlank String surname
) {
}
