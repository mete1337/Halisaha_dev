package com.halisaha.backend.service.Abstract;

import com.halisaha.backend.dto.SubPitchCreateRequest;
import com.halisaha.backend.dto.SubPitchResponse;

public interface ISubPitchService {
    SubPitchResponse createSubPitchAsOwner(SubPitchCreateRequest request, String ownerUsername);
    SubPitchResponse approveSubPitch(Long subPitchId);
}
