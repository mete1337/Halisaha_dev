package com.halisaha.backend.service;

import com.halisaha.backend.converter.SubPitchConverter;
import com.halisaha.backend.dto.SubPitchCreateRequest;
import com.halisaha.backend.dto.SubPitchResponse;
import com.halisaha.backend.model.Pitch;
import com.halisaha.backend.model.SubPitch;
import com.halisaha.backend.repository.PitchRepository;
import com.halisaha.backend.repository.SubPitchRepository;
import com.halisaha.backend.service.Abstract.ISubPitchService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubPitchService implements ISubPitchService {

    private final PitchRepository pitchRepository;
    private final SubPitchRepository subPitchRepository;
    private final SubPitchConverter subPitchConverter;

    @Override
    public SubPitchResponse createSubPitchAsOwner(SubPitchCreateRequest request, String ownerUsername) {
        try {
            Pitch pitch = pitchRepository.findById(request.pitchId())
                    .orElseThrow(() -> new EntityNotFoundException("Pitch not found"));

            if (!pitch.getOwner().getUsername().equals(ownerUsername)) {
                log.warn("SubPitch create rejected: user {} is not owner of pitch {}", ownerUsername, request.pitchId());
                throw new RuntimeException("You can only create subpitch for your own pitch");
            }

            var savedSubPitch = subPitchRepository.save(subPitchConverter.toModel(request, pitch));
            log.info("SubPitch {} created by owner {}", savedSubPitch.getId(), ownerUsername);
            return subPitchConverter.toResponse(savedSubPitch);
        } catch (RuntimeException ex) {
            log.warn("SubPitch create failed by owner {} for pitch {}: {}", ownerUsername, request.pitchId(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while creating subpitch for owner {}", ownerUsername, ex);
            throw ex;
        }
    }

    @Override
    public SubPitchResponse approveSubPitch(Long subPitchId) {
        try {
            SubPitch subPitch = subPitchRepository.findById(subPitchId)
                    .orElseThrow(() -> new EntityNotFoundException("SubPitch not found"));

            subPitch.setIsActive(true);
            var savedSubPitch = subPitchRepository.save(subPitch);
            log.info("SubPitch {} approved by admin", savedSubPitch.getId());
            return subPitchConverter.toResponse(savedSubPitch);
        } catch (RuntimeException ex) {
            log.warn("SubPitch approve failed for id {}: {}", subPitchId, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while approving subpitch {}", subPitchId, ex);
            throw ex;
        }
    }
}
