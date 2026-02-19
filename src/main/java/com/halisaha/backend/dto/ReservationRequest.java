package com.halisaha.backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationRequest(
        Integer subPitchId, // pitchId yerine subPitchId oldu
        LocalDate bookingDate,
        LocalTime startTime,
        LocalTime endTime
) {}