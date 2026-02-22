package com.halisaha.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record PitchRequest(
        @NotBlank(message = "Saha adı zorunludur")
        String name,

        String phoneNumber,

        @NotBlank(message = "Şehir zorunludur")
        String city,

        @NotBlank(message = "İlçe zorunludur")
        String district,

        @NotBlank(message = "Adres zorunludur")
        String address,

        @NotNull(message = "Açılış saati zorunludur")
        LocalTime openTime,

        @NotNull(message = "Kapanış saati zorunludur")
        LocalTime closeTime,

        Boolean isActive,

        String description,

        String imageUrl
) {
}
