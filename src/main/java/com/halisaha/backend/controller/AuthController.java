package com.halisaha.backend.controller;

import com.halisaha.backend.dto.LoginRequest;
import com.halisaha.backend.dto.RegisterRequest;
import com.halisaha.backend.model.Role;
import com.halisaha.backend.model.User;
import com.halisaha.backend.repository.UserRepository;
import com.halisaha.backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // --- KAYIT OL (REGISTER) İŞLEMİ ---
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        // YENİ EKLENDİ: Veritabanı çökmesin diye verilerin boş gelip gelmediğini kontrol ediyoruz
        if (request.name() == null || request.surname() == null || request.phoneNumber() == null) {
            return ResponseEntity.badRequest().body(Map.of("hata", "Ad, soyad ve telefon numarası zorunludur!"));
        }

        // 1. Email veritabanında zaten var mı kontrol et
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("hata", "Bu email adresi zaten kullanımda!"));
        }

        // 2. Yeni kullanıcıyı oluştur ve şifresini hash'le
        User newUser = User.builder()
                .name(request.name())               // YENİ EKLENDİ
                .surname(request.surname())         // YENİ EKLENDİ
                .phoneNumber(request.phoneNumber()) // YENİ EKLENDİ
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role() != null ? request.role() : Role.USER)
                .build();

        // 3. Veritabanına kaydet
        userRepository.save(newUser);

        // 4. Kayıt başarılı olunca direkt token üretip geri dön
        String token = jwtService.generateToken(newUser.getEmail());

        return ResponseEntity.ok(Map.of(
                "mesaj", "Kayıt işlemi başarıyla tamamlandı.",
                "token", token
        ));
    }

    // --- GİRİŞ YAP (LOGIN) İŞLEMİ (Mevcut kodun) ---
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        String token = jwtService.generateToken(request.email());
        return ResponseEntity.ok(Map.of("token", token));
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