package com.halisaha.backend.repository;

import com.halisaha.backend.model.SubPitch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubPitchRepository extends JpaRepository<SubPitch, Long> {
    List<SubPitch> findAllByPitch_Owner_IdAndIsActiveTrue(Long ownerId);
}
