package com.halisaha.backend.service;

import lombok.extern.slf4j.Slf4j;
import com.halisaha.backend.dto.AuthResponse;
import com.halisaha.backend.dto.LoginRequest;
import com.halisaha.backend.dto.RegisterRequest;
import com.halisaha.backend.model.Role;
import com.halisaha.backend.model.User;
import com.halisaha.backend.repository.UserRepository;
import com.halisaha.backend.service.Abstract.IAuthService;
import com.halisaha.backend.service.Abstract.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public void register(RegisterRequest request) {
        try {
            if (userRepository.existsByUsername(request.username())) {
                log.warn("Register rejected: username {} already exists", request.username());
                throw new IllegalArgumentException("Username is already taken");
            }

            if (userRepository.existsByEmail(request.email())) {
                log.warn("Register rejected: email {} already exists", request.email());
                throw new IllegalArgumentException("Email is already in use");
            }
            if (userRepository.existsByPhoneNumber(request.phoneNumber())){
                log.warn("Register rejected: phone number {} already exists", request.phoneNumber());
                throw new IllegalArgumentException("Phone number is already in use");
            }

            User user = User.builder()
                    .username(request.username())
                    .password(passwordEncoder.encode(request.password()))
                    .role(Role.USER)
                    .email(request.email())
                    .phoneNumber(request.phoneNumber())
                    .name(request.name())
                    .surname(request.surname())
                    .build();

            userRepository.save(user);
            log.info("User {} registered", user.getUsername());
        } catch (IllegalArgumentException ex) {
            log.warn("Register failed for username {}: {}", request.username(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while registering user {}", request.username(), ex);
            throw ex;
        }
    }

    @Override
    public AuthResponse authenticate(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
        } catch (AuthenticationException ex) {
            log.warn("Authentication failed for username {}", request.username());
            throw new IllegalArgumentException("Invalid username or password");
        }

        try {
            User user = userRepository.findByUsername(request.username())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            String token = jwtService.generateToken(user);
            log.info("JWT token generated for user {}", user.getUsername());
            return new AuthResponse(token);
        } catch (IllegalArgumentException ex) {
            log.warn("Authentication flow failed for username {}: {}", request.username(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while authenticating username {}", request.username(), ex);
            throw ex;
        }
    }
}
