package com.halisaha.backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationRequest(
        Long subPitchId,
        LocalDate bookingDate,
        LocalTime startTime,
        LocalTime endTime
) {}