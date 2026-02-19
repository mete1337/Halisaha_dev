package com.halisaha.backend.repository;

import com.halisaha.backend.model.Pitch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PitchRepository extends JpaRepository<Pitch, Integer> {
}