package com.halisaha.backend.service;

import com.halisaha.backend.dto.PitchRequest;
import com.halisaha.backend.dto.PitchResponse;

public interface IPitchService {

    public PitchResponse createPitch(PitchRequest request, String username);
}
