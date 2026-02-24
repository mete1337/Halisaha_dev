package com.halisaha.backend.converter;

import com.halisaha.backend.dto.ReservationRequest;
import com.halisaha.backend.dto.ReservationResponse;
import com.halisaha.backend.model.Reservation;
import com.halisaha.backend.model.SubPitch;
import com.halisaha.backend.model.User;
import org.springframework.stereotype.Component;

@Component
public class ReservationConverter {

    public Reservation toModel(ReservationRequest request, User user, SubPitch subPitch) {
        return Reservation.builder()
                .subPitch(subPitch)
                .user(user)
                .bookingDate(request.bookingDate())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .totalPrice(subPitch.getPricePerHour())
                .build();
    }

    public ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getSubPitch().getPitch().getName(),
                reservation.getSubPitch().getName(),
                reservation.getBookingDate(),
                reservation.getStartTime(),
                reservation.getEndTime()
        );
    }
}
