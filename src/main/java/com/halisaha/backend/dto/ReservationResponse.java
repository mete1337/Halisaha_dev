package com.halisaha.backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResponse(
        Integer id,
        String pitchName,
        LocalDate bookingDate,
        LocalTime startTime,
        LocalTime endTime
) {}