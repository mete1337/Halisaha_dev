package com.halisaha.backend.dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long reservationId,
        Long pitchId,
        String pitchName,
        Integer rating,
        String comment,
        LocalDateTime createdAt
) {
}
