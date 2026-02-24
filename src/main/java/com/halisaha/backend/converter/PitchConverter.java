package com.halisaha.backend.converter;

import com.halisaha.backend.dto.AdminPitchCreateRequest;
import com.halisaha.backend.dto.PitchResponse;
import com.halisaha.backend.model.Pitch;
import com.halisaha.backend.model.User;
import org.springframework.stereotype.Component;

@Component
public class PitchConverter {

    public Pitch toModel(AdminPitchCreateRequest request, User owner) {
        return Pitch.builder()
                .owner(owner)
                .name(request.name())
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
    }

    public PitchResponse toResponse(Pitch pitch) {
        return PitchResponse.builder()
                .id(pitch.getId())
                .ownerId(pitch.getOwner().getId())
                .ownerName(pitch.getOwner().getName())
                .name(pitch.getName())
                .phoneNumber(pitch.getPhoneNumber())
                .city(pitch.getCity())
                .district(pitch.getDistrict())
                .address(pitch.getAddress())
                .openTime(pitch.getOpenTime())
                .closeTime(pitch.getCloseTime())
                .isActive(pitch.getIsActive())
                .description(pitch.getDescription())
                .imageUrl(pitch.getImageUrl())
                .createdAt(pitch.getCreatedAt())
                .build();
    }
}
