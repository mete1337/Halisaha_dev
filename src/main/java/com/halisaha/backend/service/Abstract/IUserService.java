package com.halisaha.backend.service.Abstract;

import com.halisaha.backend.dto.UserProfileResponse;
import com.halisaha.backend.dto.UserProfileUpdateRequest;

public interface IUserService {
    UserProfileResponse getMyProfile(String username);
    UserProfileResponse updateMyProfile(String username, UserProfileUpdateRequest request);
    void deactivateMyAccount(String username);
}
