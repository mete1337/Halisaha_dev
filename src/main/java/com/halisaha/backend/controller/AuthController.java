package com.halisaha.backend.controller;

import com.halisaha.backend.dto.LoginRequest;
import com.halisaha.backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/auth/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        String token = jwtService.generateToken(request.email());
        return Map.of("token", token);
    }

    // --- TEST UÇ NOKTALARI ---
    @GetMapping("/user/test")
    public String userTest() {
        return "Giriş yapmış herhangi bir kullanıcı bu yazıyı görebilir.";
    }

    @GetMapping("/owner/test")
    public String ownerTest() {
        return "Sadece OWNER veya ADMIN rollerindeki hesaplar bu yazıyı görebilir.";
    }

    @GetMapping("/admin/test")
    public String adminTest() {
        return "Sadece ADMIN bu yazıyı görebilir.";
    }
}