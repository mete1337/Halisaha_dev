package com.halisaha.backend.service;

import com.halisaha.backend.dto.AdminPitchCreateRequest;
import com.halisaha.backend.dto.PitchResponse;
import com.halisaha.backend.dto.SubPitchCreateRequest;
import com.halisaha.backend.dto.SubPitchResponse;
import com.halisaha.backend.model.Pitch;
import com.halisaha.backend.model.Role;
import com.halisaha.backend.model.SubPitch;
import com.halisaha.backend.model.User;
import com.halisaha.backend.repository.PitchRepository;
import com.halisaha.backend.repository.SubPitchRepository;
import com.halisaha.backend.repository.UserRepository;
import com.halisaha.backend.service.Abstract.IPitchService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PitchService implements IPitchService {

    private final UserRepository userRepository;
    private final PitchRepository pitchRepository;
    private final SubPitchRepository subPitchRepository;


    @Override
    public PitchResponse createPitchAsAdmin(AdminPitchCreateRequest request){
        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new EntityNotFoundException("Owner not found"));
        if (owner.getRole() != Role.OWNER) {
            throw new RuntimeException("Pitch owner must have OWNER role");
        }

        Pitch newPitch = Pitch.builder()
                .name(request.name())
                .owner(owner)
                .phoneNumber(request.phoneNumber())
                .city(request.city())
                .district(request.district())
                .address(request.address())
                .openTime(request.openTime())
                .closeTime(request.closeTime())
                .isActive(request.isActive() == null ? true : request.isActive())
                .description(request.description())
                .imageUrl(request.imageUrl())
                .build();

        pitchRepository.save(newPitch);

        return PitchResponse.builder()
                .id(newPitch.getId())
                .ownerId(newPitch.getOwner().getId())
                .ownerName(newPitch.getOwner().getName())
                .name(newPitch.getName())
                .phoneNumber(newPitch.getPhoneNumber())
                .city(newPitch.getCity())
                .district(newPitch.getDistrict())
                .address(newPitch.getAddress())
                .openTime(newPitch.getOpenTime())
                .closeTime(newPitch.getCloseTime())
                .description(newPitch.getDescription())
                .imageUrl(newPitch.getImageUrl())
                .createdAt(newPitch.getCreatedAt())
                .build();
    }

    @Override
    public SubPitchResponse createSubPitchAsOwner(SubPitchCreateRequest request, String ownerUsername) {
        Pitch pitch = pitchRepository.findById(request.pitchId())
                .orElseThrow(() -> new EntityNotFoundException("Pitch not found"));

        if (!pitch.getOwner().getUsername().equals(ownerUsername)) {
            throw new RuntimeException("You can only create subpitch for your own pitch");
        }

        SubPitch subPitch = SubPitch.builder()
                .pitch(pitch)
                .name(request.name())
                .isCovered(request.isCovered())
                .pricePerHour(request.pricePerHour())
                .isActive(false)
                .build();

        SubPitch saved = subPitchRepository.save(subPitch);

        return new SubPitchResponse(
                saved.getId(),
                saved.getPitch().getId(),
                saved.getPitch().getName(),
                saved.getName(),
                saved.getIsCovered(),
                saved.getPricePerHour(),
                saved.getIsActive()
        );
    }

    @Override
    public SubPitchResponse approveSubPitch(Long subPitchId) {
        SubPitch subPitch = subPitchRepository.findById(subPitchId)
                .orElseThrow(() -> new EntityNotFoundException("SubPitch not found"));

        subPitch.setIsActive(true);
        SubPitch saved = subPitchRepository.save(subPitch);

        return new SubPitchResponse(
                saved.getId(),
                saved.getPitch().getId(),
                saved.getPitch().getName(),
                saved.getName(),
                saved.getIsCovered(),
                saved.getPricePerHour(),
                saved.getIsActive()
        );
    }
}
