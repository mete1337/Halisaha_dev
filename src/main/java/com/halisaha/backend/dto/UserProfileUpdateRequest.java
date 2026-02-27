package com.halisaha.backend.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserProfileUpdateRequest(
        @Pattern(regexp = "^05\\d{9}$", message = "Phone number must be in format 05XXXXXXXXX")
        String phoneNumber,

        String currentPassword,

        @Size(min = 6, message = "New password must be at least 6 characters")
        String newPassword,

        String newPasswordConfirm
) {
}
