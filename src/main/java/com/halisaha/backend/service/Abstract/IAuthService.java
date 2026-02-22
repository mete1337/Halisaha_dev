package com.halisaha.backend.service.Abstract;

import com.halisaha.backend.dto.AuthResponse;
import com.halisaha.backend.dto.LoginRequest;
import com.halisaha.backend.dto.RegisterRequest;

public interface IAuthService {
    void register(RegisterRequest request);
    AuthResponse authenticate(LoginRequest request);
}
