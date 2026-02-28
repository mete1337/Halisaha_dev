package com.halisaha.backend.controller;

import com.halisaha.backend.dto.UserProfileResponse;
import com.halisaha.backend.dto.UserProfileUpdateRequest;
import com.halisaha.backend.service.Abstract.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final IUserService userService;

    @GetMapping("/ping")
    @ResponseStatus(HttpStatus.OK)
    public String ping() {
        return "User endpoint: authenticated";
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponse getMyProfile(Principal principal) {
        log.info("GET /api/user/me requested by {}", principal.getName());
        return userService.getMyProfile(principal.getName());
    }

    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponse updateMyProfile(@RequestBody @Valid UserProfileUpdateRequest request, Principal principal) {
        try {
            log.info("PUT /api/user/me requested by {}", principal.getName());
            return userService.updateMyProfile(principal.getName(), request);
        } catch (IllegalArgumentException ex) {
            log.warn("PUT /api/user/me failed for {}: {}", principal.getName(), ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateMyAccount(Principal principal) {
        try {
            log.info("DELETE /api/user/me requested by {}", principal.getName());
            userService.deactivateMyAccount(principal.getName());
        } catch (IllegalArgumentException ex) {
            log.warn("DELETE /api/user/me failed for {}: {}", principal.getName(), ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
