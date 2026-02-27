package com.halisaha.backend.converter;

import com.halisaha.backend.dto.UserProfileResponse;
import com.halisaha.backend.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserProfileResponse toProfileResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole()
        );
    }
}
