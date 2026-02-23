package com.halisaha.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubPitchCreateRequest(
        @NotNull Long pitchId,
        @NotBlank String name,
        @NotNull Boolean isCovered,
        @NotNull @Min(1) Double pricePerHour
) {
}
