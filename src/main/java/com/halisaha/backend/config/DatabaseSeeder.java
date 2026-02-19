package com.halisaha.backend.config;

import com.halisaha.backend.model.*;
import com.halisaha.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PitchRepository pitchRepository;
    private final SubPitchRepository subPitchRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {

            // 1. OWNER Hesabı
            User owner = User.builder()
                    .name("Ahmet")
                    .surname("Yılmaz")
                    .username("ahmetyilmaz")
                    .phoneNumber("05551112233")
                    .email("owner@test.com")
                    .password(passwordEncoder.encode("1234"))
                    .role(Role.OWNER)
                    .build();
            userRepository.save(owner);

            // 2. USER Hesabı
            User user = User.builder()
                    .name("Can")
                    .surname("Kaya")
                    .username("cankaya")
                    .phoneNumber("05559998877")
                    .email("user@test.com")
                    .password(passwordEncoder.encode("1234"))
                    .role(Role.USER)
                    .build();
            userRepository.save(user);

            // 3. Örnek Ana Tesis (Pitch) Oluşturma
            Pitch anaTesis = Pitch.builder()
                    .owner(owner)
                    .name("Yıldızlar Arena Halı Saha")
                    .phoneNumber("02324445566")
                    .city("İzmir")
                    .district("Buca")
                    .address("Şirinyer Mah. No:45")
                    .description("İzmir'in en modern halı saha tesisi. Ücretsiz otopark mevcuttur.")
                    .openTime(LocalTime.of(9, 0))
                    .closeTime(LocalTime.of(2, 0))
                    .build();
            pitchRepository.save(anaTesis);

            // 4. Ana Tesise Ait 1. Alt Saha (A Sahası - Kapalı)
            SubPitch altSaha1 = SubPitch.builder()
                    .pitch(anaTesis) // Saha bu tesise ait
                    .name("A Sahası (Kapalı)")
                    .isCovered(true) // Üstü kapalı
                    .pricePerHour(1200.0) // Saatlik 1200 TL
                    .build();
            subPitchRepository.save(altSaha1);

            // 5. Ana Tesise Ait 2. Alt Saha (B Sahası - Açık)
            SubPitch altSaha2 = SubPitch.builder()
                    .pitch(anaTesis) // Bu saha da aynı tesise ait
                    .name("B Sahası (Açık)")
                    .isCovered(false) // Üstü açık
                    .pricePerHour(1000.0) // Saatlik 1000 TL
                    .build();
            subPitchRepository.save(altSaha2);

            System.out.println("✅ Profesyonel ERD yapısına uygun veritabanı 2 farklı alt saha ile oluşturuldu!");
        }
    }
}