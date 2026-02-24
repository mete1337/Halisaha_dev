package com.halisaha.backend.service;

import com.halisaha.backend.converter.PitchConverter;
import com.halisaha.backend.dto.AdminPitchCreateRequest;
import com.halisaha.backend.dto.PitchResponse;
import com.halisaha.backend.model.Role;
import com.halisaha.backend.model.User;
import com.halisaha.backend.repository.PitchRepository;
import com.halisaha.backend.repository.UserRepository;
import com.halisaha.backend.service.Abstract.IPitchService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class PitchService implements IPitchService {

    private final UserRepository userRepository;
    private final PitchRepository pitchRepository;
    private final PitchConverter pitchConverter;


    @Override
    public PitchResponse createPitchAsAdmin(AdminPitchCreateRequest request){
        try {
            User owner = userRepository.findById(request.ownerId())
                    .orElseThrow(() -> new EntityNotFoundException("Owner not found"));
            if (owner.getRole() != Role.OWNER) {
                log.warn("Pitch create rejected: ownerId {} is not OWNER", request.ownerId());
                throw new RuntimeException("Pitch owner must have OWNER role");
            }

            var savedPitch = pitchRepository.save(pitchConverter.toModel(request, owner));
            log.info("Pitch {} created by admin for owner {}", savedPitch.getId(), owner.getUsername());
            return pitchConverter.toResponse(savedPitch);
        } catch (RuntimeException ex) {
            log.warn("Pitch create failed for ownerId {}: {}", request.ownerId(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while creating pitch for ownerId {}", request.ownerId(), ex);
            throw ex;
        }
    }
}
