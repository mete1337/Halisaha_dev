package com.halisaha.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Login ve Register herkese açık

                        // 1. Sadece ADMIN girebilir
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 2. OWNER uç noktasına OWNER ve ADMIN girebilir (USER giremez)
                        .requestMatchers("/api/owner/**").hasAnyRole("OWNER", "ADMIN")

                        // 3. USER uç noktasına USER ve ADMIN girebilir (OWNER giremez)
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")

                        // Geri kalan her şey için giriş yapmış olmak yeterli
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}