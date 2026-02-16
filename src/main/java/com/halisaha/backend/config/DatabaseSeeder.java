package com.halisaha.backend.config;

import com.halisaha.backend.model.Role;
import com.halisaha.backend.model.User;
import com.halisaha.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) { // Veritabanı boşsa çalışır

            // ADMIN Hesabı
            var admin = User.builder()
                    .email("admin@test.com")
                    .password(passwordEncoder.encode("1234"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);

            // OWNER Hesabı
            var owner = User.builder()
                    .email("owner@test.com")
                    .password(passwordEncoder.encode("1234"))
                    .role(Role.OWNER)
                    .build();
            userRepository.save(owner);

            // USER Hesabı
            var user = User.builder()
                    .email("user@test.com")
                    .password(passwordEncoder.encode("1234"))
                    .role(Role.USER)
                    .build();
            userRepository.save(user);

            System.out.println("✅ Varsayılan hesaplar oluşturuldu!");
        }
    }
}