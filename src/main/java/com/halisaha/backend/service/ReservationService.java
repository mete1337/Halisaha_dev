package com.halisaha.backend.service;

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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final SubPitchRepository subPitchRepository;
    private final UserRepository userRepository;

    @Override
    public ReservationResponse createReservation(ReservationRequest request, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User could not find for the reservation"));

        SubPitch subPitch = subPitchRepository.findById(request.subPitchId())
                .orElseThrow(() -> new RuntimeException("The Subpitch requested for reservation is not exist"));
        if (!Boolean.TRUE.equals(subPitch.getIsActive())) {
            throw new RuntimeException("Subpitch is not active yet, admin approval required");
        }

        boolean isBooked = reservationRepository.existsBySubPitchIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
                request.subPitchId(),
                request.bookingDate(),
                request.endTime(),
                request.startTime()
        );

        if (isBooked) {
            throw new RuntimeException("That time is already occupied for the subpitch");
        }
//Toplam rezervasyon saati konuşulacak
        Reservation newReservation = Reservation.builder()
                .subPitch(subPitch)
                .user(user)
                .bookingDate(request.bookingDate())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .totalPrice(subPitch.getPricePerHour()) // Fiyatı o anki haliyle kaydediyoruz
                .build();

        reservationRepository.save(newReservation);
        return ReservationResponse.builder()
                .id(newReservation.getId())
                .pitchName(subPitch.getPitch().getName())
                .subPitchName(subPitch.getName())
                .bookingDate(newReservation.getBookingDate())
                .startTime(newReservation.getStartTime())
                .endTime(newReservation.getEndTime())
                .build();
    }

    @Override
    public List<ReservationResponse> getUserReservations(String username) {
        return reservationRepository.findAllByUser_username(username)
                .stream()
                .map(res -> new ReservationResponse(
                        res.getId(),
                        res.getSubPitch().getPitch().getName(),
                        res.getSubPitch().getName(),
                        res.getBookingDate(),
                        res.getStartTime(),
                        res.getEndTime()
                ))
                .toList();
    }
}
