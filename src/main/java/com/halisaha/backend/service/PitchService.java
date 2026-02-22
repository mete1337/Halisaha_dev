package com.halisaha.backend.service;

import com.halisaha.backend.dto.PitchRequest;
import com.halisaha.backend.dto.PitchResponse;
import com.halisaha.backend.model.Pitch;
import com.halisaha.backend.model.User;
import com.halisaha.backend.repository.PitchRepository;
import com.halisaha.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PitchService implements IPitchService {

    private final UserRepository userRepository;
    private final PitchRepository pitchRepository;


    @Override
    public PitchResponse createPitch(PitchRequest request, String username){

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));

        Pitch newPitch = Pitch.builder()
                .name(request.name())
                .owner(user)
                .phoneNumber(request.phoneNumber())
                .city(request.city())
                .district(request.district())
                .address(request.address())
                .openTime(request.openTime())
                .closeTime(request.closeTime())
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

}
