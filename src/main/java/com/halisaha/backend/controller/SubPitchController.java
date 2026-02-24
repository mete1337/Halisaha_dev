package com.halisaha.backend.controller;

import com.halisaha.backend.dto.SubPitchCreateRequest;
import com.halisaha.backend.dto.SubPitchResponse;
import com.halisaha.backend.service.Abstract.ISubPitchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class SubPitchController {

    private final ISubPitchService subPitchService;

    @PostMapping("/api/owner/subpitch/create")
    @ResponseStatus(HttpStatus.CREATED)
    public SubPitchResponse createSubPitch(@RequestBody @Valid SubPitchCreateRequest request, Principal principal) {
        try {
            return subPitchService.createSubPitchAsOwner(request, principal.getName());
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PatchMapping("/api/admin/subpitch/{subPitchId}/approve")
    @ResponseStatus(HttpStatus.OK)
    public SubPitchResponse approveSubPitch(@PathVariable Long subPitchId) {
        try {
            return subPitchService.approveSubPitch(subPitchId);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
