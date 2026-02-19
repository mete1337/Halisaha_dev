package com.halisaha.backend.dto;

import com.halisaha.backend.model.Role;

public record RegisterRequest(
        String name,        // Eklendi (Zorunlu)
        String surname,     // Eklendi (Zorunlu)
        String phoneNumber, // Eklendi (Zorunlu)
        String email,
        String password,
        Role role
) {}