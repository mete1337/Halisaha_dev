package com.halisaha.backend.service;

import com.halisaha.backend.converter.ReservationConverter;
import com.halisaha.backend.dto.ReservationRequest;
import com.halisaha.backend.dto.ReservationResponse;
import com.halisaha.backend.model.SubPitch;
import com.halisaha.backend.model.Reservation;
import com.halisaha.backend.model.User;
import com.halisaha.backend.repository.SubPitchRepository;
import com.halisaha.backend.repository.ReservationRepository;
import com.halisaha.backend.repository.UserRepository;
import com.halisaha.backend.service.Abstract.IReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final SubPitchRepository subPitchRepository;
    private final UserRepository userRepository;
    private final ReservationConverter reservationConverter;

    @Override
    public ReservationResponse createReservation(ReservationRequest request, String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User could not find for the reservation"));

            SubPitch subPitch = subPitchRepository.findById(request.subPitchId())
                    .orElseThrow(() -> new RuntimeException("The Subpitch requested for reservation is not exist"));
            if (!Boolean.TRUE.equals(subPitch.getIsActive())) {
                log.warn("Reservation rejected: subPitch {} not active for user {}", request.subPitchId(), username);
                throw new RuntimeException("Subpitch is not active yet, admin approval required");
            }

            boolean isBooked = reservationRepository.existsBySubPitchIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
                    request.subPitchId(),
                    request.bookingDate(),
                    request.endTime(),
                    request.startTime()
            );

            if (isBooked) {
                log.warn("Reservation rejected: slot occupied. user {}, subPitch {}, date {}, {}-{}",
                        username, request.subPitchId(), request.bookingDate(), request.startTime(), request.endTime());
                throw new RuntimeException("That time is already occupied for the subpitch");
            }

            Reservation newReservation = reservationConverter.toModel(request, user, subPitch);
            var savedReservation = reservationRepository.save(newReservation);
            log.info("Reservation {} created by user {}", savedReservation.getId(), username);
            return reservationConverter.toResponse(savedReservation);
        } catch (RuntimeException ex) {
            log.warn("Reservation create failed for user {}: {}", username, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while creating reservation for user {}", username, ex);
            throw ex;
        }
    }

    @Override
    public List<ReservationResponse> getUserReservations(String username) {
        try {
            List<ReservationResponse> reservations = reservationRepository.findAllByUser_username(username)
                    .stream()
                    .map(reservationConverter::toResponse)
                    .toList();
            log.info("Reservations listed for user {}", username);
            return reservations;
        } catch (RuntimeException ex) {
            log.warn("Reservation list failed for user {}: {}", username, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while listing reservations for user {}", username, ex);
            throw ex;
        }
    }
}
