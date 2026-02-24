package com.halisaha.backend.converter;

import com.halisaha.backend.dto.SubPitchCreateRequest;
import com.halisaha.backend.dto.SubPitchResponse;
import com.halisaha.backend.model.Pitch;
import com.halisaha.backend.model.SubPitch;
import org.springframework.stereotype.Component;

@Component
public class SubPitchConverter {

    public SubPitch toModel(SubPitchCreateRequest request, Pitch pitch) {
        return SubPitch.builder()
                .pitch(pitch)
                .name(request.name())
                .isCovered(request.isCovered())
                .pricePerHour(request.pricePerHour())
                .isActive(false)
                .build();
    }

    public SubPitchResponse toResponse(SubPitch subPitch) {
        return new SubPitchResponse(
                subPitch.getId(),
                subPitch.getPitch().getId(),
                subPitch.getPitch().getName(),
                subPitch.getName(),
                subPitch.getIsCovered(),
                subPitch.getPricePerHour(),
                subPitch.getIsActive()
        );
    }
}
