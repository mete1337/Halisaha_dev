package com.halisaha.backend.service;

import com.halisaha.backend.converter.UserConverter;
import com.halisaha.backend.dto.UserProfileResponse;
import com.halisaha.backend.dto.UserProfileUpdateRequest;
import com.halisaha.backend.model.Pitch;
import com.halisaha.backend.model.Role;
import com.halisaha.backend.model.SubPitch;
import com.halisaha.backend.model.User;
import com.halisaha.backend.repository.PitchRepository;
import com.halisaha.backend.repository.SubPitchRepository;
import com.halisaha.backend.repository.UserRepository;
import com.halisaha.backend.service.Abstract.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserConverter userConverter;
    private final PitchRepository pitchRepository;
    private final SubPitchRepository subPitchRepository;

    @Override
    public UserProfileResponse getMyProfile(String username) {
        log.info("Profile fetch requested for user {}", username);
        User user = getUserByUsername(username);
        return userConverter.toProfileResponse(user);
    }

    @Override
    public UserProfileResponse updateMyProfile(String username, UserProfileUpdateRequest request) {
        try {
            User user = getUserByUsername(username);

            boolean updated = false;
            boolean phoneUpdated = false;
            boolean passwordUpdated = false;
            if (request.phoneNumber() != null && !request.phoneNumber().isBlank()) {
                if (!request.phoneNumber().equals(user.getPhoneNumber()) &&
                        userRepository.existsByPhoneNumber(request.phoneNumber())) {
                    log.warn("Profile update rejected for user {}: phone number already in use", username);
                    throw new IllegalArgumentException("Phone number is already in use");
                }
                user.setPhoneNumber(request.phoneNumber());
                updated = true;
                phoneUpdated = true;
            }

            boolean hasPasswordField =
                    request.currentPassword() != null ||
                            request.newPassword() != null ||
                            request.newPasswordConfirm() != null;

            if (hasPasswordField) {
                if (isBlank(request.currentPassword()) || isBlank(request.newPassword()) || isBlank(request.newPasswordConfirm())) {
                    log.warn("Password update rejected for user {}: missing password fields", username);
                    throw new IllegalArgumentException("To change password, currentPassword, newPassword and newPasswordConfirm are required");
                }
                if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
                    log.warn("Password update rejected for user {}: current password mismatch", username);
                    throw new IllegalArgumentException("Current password is incorrect");
                }
                if (!request.newPassword().equals(request.newPasswordConfirm())) {
                    log.warn("Password update rejected for user {}: new password confirmation mismatch", username);
                    throw new IllegalArgumentException("New passwords do not match");
                }

                user.setPassword(passwordEncoder.encode(request.newPassword()));
                updated = true;
                passwordUpdated = true;
            }

            if (updated) {
                userRepository.save(user);
                log.info(
                        "Profile updated for user {} (phoneUpdated={}, passwordUpdated={})",
                        username,
                        phoneUpdated,
                        passwordUpdated
                );
            } else {
                log.info("Profile update request had no changes for user {}", username);
            }

            return userConverter.toProfileResponse(user);
        } catch (RuntimeException ex) {
            log.warn("Profile update failed for user {}: {}", username, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while updating profile for user {}", username, ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public void deactivateMyAccount(String username) {
        try {
            User user = getUserByUsername(username);
            if (Boolean.FALSE.equals(user.getIsActive())) {
                log.info("Deactivate skipped: user {} is already inactive", username);
                return;
            }

            int deactivatedPitchCount = 0;
            int deactivatedSubPitchCount = 0;
            if (user.getRole() == Role.OWNER) {
                List<SubPitch> ownerSubPitches = subPitchRepository.findAllByPitch_Owner_IdAndIsActiveTrue(user.getId());
                ownerSubPitches.forEach(subPitch -> subPitch.setIsActive(false));
                subPitchRepository.saveAll(ownerSubPitches);
                deactivatedSubPitchCount = ownerSubPitches.size();

                List<Pitch> ownerPitches = pitchRepository.findAllByOwnerIdAndIsActiveTrue(user.getId());
                ownerPitches.forEach(pitch -> pitch.setIsActive(false));
                pitchRepository.saveAll(ownerPitches);
                deactivatedPitchCount = ownerPitches.size();
            }

            user.setIsActive(false);
            userRepository.save(user);
            log.info(
                    "Account deactivated for user {} (role={}, deactivatedPitchCount={}, deactivatedSubPitchCount={})",
                    username,
                    user.getRole(),
                    deactivatedPitchCount,
                    deactivatedSubPitchCount
            );
        } catch (RuntimeException ex) {
            log.warn("Account deactivation failed for user {}: {}", username, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while deactivating account for user {}", username, ex);
            throw ex;
        }
    }

    private User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        log.debug("User {} loaded for profile operation", username);
        return user;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
