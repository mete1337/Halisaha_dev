package com.halisaha.backend.dto;

public record SubPitchResponse(
        Long id,
        Long pitchId,
        String pitchName,
        String name,
        Boolean isCovered,
        Double pricePerHour,
        Boolean isActive
) {
}
