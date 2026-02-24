package com.halisaha.backend.service.Abstract;

import com.halisaha.backend.dto.AdminPitchCreateRequest;
import com.halisaha.backend.dto.PitchResponse;

public interface IPitchService {

    PitchResponse createPitchAsAdmin(AdminPitchCreateRequest request);
}
