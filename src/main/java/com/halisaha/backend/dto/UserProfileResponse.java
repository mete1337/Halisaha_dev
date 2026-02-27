package com.halisaha.backend.dto;

import com.halisaha.backend.model.Role;

public record UserProfileResponse(
        Long id,
        String username,
        String name,
        String surname,
        String email,
        String phoneNumber,
        Role role
) {
}
