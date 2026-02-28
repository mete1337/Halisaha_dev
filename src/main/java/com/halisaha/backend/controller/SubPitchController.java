package com.halisaha.backend.controller;

import com.halisaha.backend.dto.SubPitchCreateRequest;
import com.halisaha.backend.dto.SubPitchResponse;
import com.halisaha.backend.service.Abstract.ISubPitchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/api/subpitch")
@RequiredArgsConstructor
public class SubPitchController {

    private final ISubPitchService subPitchService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('OWNER')")
    @ResponseStatus(HttpStatus.CREATED)
    public SubPitchResponse createSubPitch(@RequestBody @Valid SubPitchCreateRequest request, Principal principal) {
        try {
            return subPitchService.createSubPitchAsOwner(request, principal.getName());
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PatchMapping("/{subPitchId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public SubPitchResponse approveSubPitch(@PathVariable Long subPitchId) {
        try {
            return subPitchService.approveSubPitch(subPitchId);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
