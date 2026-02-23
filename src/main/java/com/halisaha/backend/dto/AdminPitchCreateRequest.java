package com.halisaha.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record AdminPitchCreateRequest(
        @NotNull Long ownerId,
        @NotBlank String name,
        String phoneNumber,
        @NotBlank String city,
        @NotBlank String district,
        @NotBlank String address,
        @NotNull LocalTime openTime,
        @NotNull LocalTime closeTime,
        Boolean isActive,
        String description,
        String imageUrl
) {
}
