package com.halisaha.backend.controller;

import com.halisaha.backend.dto.AdminPitchCreateRequest;
import com.halisaha.backend.dto.PitchResponse;
import com.halisaha.backend.service.Abstract.IPitchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/pitch")
public class PitchController {

    private final IPitchService pitchService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public PitchResponse createPitch(@RequestBody @Valid AdminPitchCreateRequest request) {
    try {
        return pitchService.createPitchAsAdmin(request);

    } catch (RuntimeException ex) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }
    }
}
