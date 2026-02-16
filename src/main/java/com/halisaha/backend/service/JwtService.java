package com.halisaha.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
public class JwtService {

    // Güvenlik için en az 256 bit uzunluğunda gizli bir anahtar (Gerçek projede env değişkeninden gelmeli)
    private static final String SECRET_KEY = "CokGizliVeUzunBirAnahtarKelimesiHalisahaProjesiIcin123456789";
    private static final long EXPIRATION_TIME = 86400000; // 24 Saat (milisaniye cinsi)
    private final ObjectMapper objectMapper = new ObjectMapper(); // Spring Web'den gelen JSON çevirici

    public String generateToken(String email) {
        try {
            long exp = (System.currentTimeMillis() + EXPIRATION_TIME) / 1000;

            String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            String payload = "{\"sub\":\"" + email + "\",\"exp\":" + exp + "}";

            String encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes(StandardCharsets.UTF_8));
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));

            String signatureData = encodedHeader + "." + encodedPayload;
            String signature = createSignature(signatureData, SECRET_KEY);

            return signatureData + "." + signature;
        } catch (Exception e) {
            throw new RuntimeException("JWT oluşturulamadı", e);
        }
    }

    public String extractUsername(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            Map payloadMap = objectMapper.readValue(payloadJson, Map.class);
            return (String) payloadMap.get("sub");
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isTokenValid(String token, String userEmail) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;

            String signatureData = parts[0] + "." + parts[1];
            String expectedSignature = createSignature(signatureData, SECRET_KEY);

            if (!expectedSignature.equals(parts[2])) return false;

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            Map payloadMap = objectMapper.readValue(payloadJson, Map.class);

            String tokenEmail = (String) payloadMap.get("sub");
            Number exp = (Number) payloadMap.get("exp");

            boolean isExpired = (exp.longValue() * 1000) < System.currentTimeMillis();

            return tokenEmail.equals(userEmail) && !isExpired;
        } catch (Exception e) {
            return false;
        }
    }

    private String createSignature(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }
}