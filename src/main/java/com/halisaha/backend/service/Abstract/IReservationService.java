package com.halisaha.backend.service.Abstract;

import com.halisaha.backend.dto.ReservationRequest;
import com.halisaha.backend.dto.ReservationResponse;

import java.util.List;

public interface IReservationService {
    ReservationResponse createReservation(ReservationRequest request, String username);
    List<ReservationResponse> getUserReservations(String username);
}
