package com.halisaha.backend.repository;

import com.halisaha.backend.model.Pitch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PitchRepository extends JpaRepository<Pitch, Long> {
    List<Pitch> findAllByOwnerIdAndIsActiveTrue(Long ownerId);
}
