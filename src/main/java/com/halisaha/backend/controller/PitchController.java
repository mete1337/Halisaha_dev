package com.halisaha.backend.controller;


import com.halisaha.backend.dto.PitchRequest;
import com.halisaha.backend.dto.PitchResponse;
import com.halisaha.backend.service.IPitchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/pitch")
public class PitchController {

    private final IPitchService pitchService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public PitchResponse createPitch(@RequestBody PitchRequest request, Principal principal) {
    try {
        return ResponseEntity.ok(pitchService.createPitch(request, principal.getName())).getBody();

    } catch (RuntimeException ex) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }
    }

}
