package com.halisaha.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SubPitch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pitch_id", nullable = false)
    private Pitch pitch;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean isCovered;

    @Column(nullable = false)
    private Double pricePerHour;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true;
}