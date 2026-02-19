package com.halisaha.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pitch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String name;

    private String phoneNumber; // Opsiyonel

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String district;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(columnDefinition = "TEXT")
    private String description; // Opsiyonel

    private String imageUrl; // Opsiyonel

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}