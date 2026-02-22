package com.halisaha.backend.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record PitchResponse(
        Integer id,
        Long ownerId,
        String ownerName,
        String name,
        String phoneNumber,
        String city,
        String district,
        String address,
        LocalTime openTime,
        LocalTime closeTime,
        Boolean isActive,
        String description,
        String imageUrl,
        LocalDateTime createdAt
) {
}
