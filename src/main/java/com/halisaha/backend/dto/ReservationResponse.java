package com.halisaha.backend.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
@Builder
public record ReservationResponse(
        Integer id,
        String pitchName,
        String subPitchName,
        LocalDate bookingDate,
        LocalTime startTime,
        LocalTime endTime
) {}