package com.halisaha.backend.controller;

import com.halisaha.backend.dto.ReservationRequest;
import com.halisaha.backend.dto.ReservationResponse;
import com.halisaha.backend.service.Abstract.IReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final IReservationService reservationService;

    @PostMapping("/create")
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest request, Principal principal) {
        try {
            return ResponseEntity.ok( reservationService.createReservation(request, principal.getName()));
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED,ex.getMessage(), ex);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<ReservationResponse>> getUserReservations(Principal principal) {
        return ResponseEntity.ok(reservationService.getUserReservations(principal.getName()));
    }
}
