package com.halisaha.backend.controller;

import com.halisaha.backend.dto.SubPitchCreateRequest;
import com.halisaha.backend.dto.SubPitchResponse;
import com.halisaha.backend.service.Abstract.IPitchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/subpitch")
public class OwnerSubPitchController {

    private final IPitchService pitchService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public SubPitchResponse createSubPitch(@RequestBody @Valid SubPitchCreateRequest request, Principal principal) {
        try {
            return pitchService.createSubPitchAsOwner(request, principal.getName());
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
