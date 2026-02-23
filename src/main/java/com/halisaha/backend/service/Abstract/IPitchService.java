package com.halisaha.backend.service.Abstract;

import com.halisaha.backend.dto.AdminPitchCreateRequest;
import com.halisaha.backend.dto.PitchResponse;
import com.halisaha.backend.dto.SubPitchCreateRequest;
import com.halisaha.backend.dto.SubPitchResponse;

public interface IPitchService {

    PitchResponse createPitchAsAdmin(AdminPitchCreateRequest request);
    SubPitchResponse createSubPitchAsOwner(SubPitchCreateRequest request, String ownerUsername);
    SubPitchResponse approveSubPitch(Long subPitchId);
}
