package com.halisaha.backend.controller;

import com.halisaha.backend.dto.ReservationRequest;
import com.halisaha.backend.dto.ReservationResponse;
import com.halisaha.backend.service.Abstract.IReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse createReservation(@RequestBody ReservationRequest request, Principal principal) {
        try {
            return reservationService.createReservation(request, principal.getName());
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED,ex.getMessage(), ex);
        }
    }

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponse> getUserReservations(Principal principal) {
        return reservationService.getUserReservations(principal.getName());
    }
}
