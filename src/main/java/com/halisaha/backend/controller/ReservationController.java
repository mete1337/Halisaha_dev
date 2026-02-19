package com.halisaha.backend.controller;

import com.halisaha.backend.dto.ReservationRequest;
import com.halisaha.backend.dto.ReservationResponse;
import com.halisaha.backend.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // --- REZERVASYON YAPMA UÇ NOKTASI ---
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequest request, Principal principal) {
        try {
            var response = reservationService.createReservation(request, principal.getName());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("hata", e.getMessage()));
        }
    }

    // --- REZERVASYONLARI LİSTELEME UÇ NOKTASI ---
    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getUserReservations(Principal principal) {
        var reservations = reservationService.getUserReservations(principal.getName());
        return ResponseEntity.ok(reservations);
    }
}