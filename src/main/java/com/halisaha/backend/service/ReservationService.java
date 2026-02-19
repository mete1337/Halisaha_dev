package com.halisaha.backend.service;

import com.halisaha.backend.dto.ReservationRequest;
import com.halisaha.backend.dto.ReservationResponse;
import com.halisaha.backend.model.SubPitch;
import com.halisaha.backend.model.Reservation;
import com.halisaha.backend.model.User;
import com.halisaha.backend.repository.SubPitchRepository;
import com.halisaha.backend.repository.ReservationRepository;
import com.halisaha.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SubPitchRepository subPitchRepository;
    private final UserRepository userRepository;

    public Map<String, String> createReservation(ReservationRequest request, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Artık Pitch değil, SubPitch (Alt saha) buluyoruz
        SubPitch subPitch = subPitchRepository.findById(request.subPitchId())
                .orElseThrow(() -> new RuntimeException("Alt saha bulunamadı"));

        boolean isBooked = reservationRepository.existsBySubPitchIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
                request.subPitchId(),
                request.bookingDate(),
                request.endTime(),
                request.startTime()
        );

        if (isBooked) {
            throw new RuntimeException("Maalesef bu saat dilimi zaten dolu!");
        }

        Reservation newReservation = Reservation.builder()
                .subPitch(subPitch)
                .user(user)
                .bookingDate(request.bookingDate())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .totalPrice(subPitch.getPricePerHour()) // Fiyatı o anki haliyle kaydediyoruz
                .build();

        reservationRepository.save(newReservation);

        return Map.of("mesaj", "Rezervasyon başarıyla oluşturuldu!");
    }

    public List<ReservationResponse> getUserReservations(String userEmail) {
        return reservationRepository.findAllByUser_Email(userEmail)
                .stream()
                .map(res -> new ReservationResponse(
                        res.getId(),
                        // Kullanıcıya Tesis Adı + Alt Saha Adını birleştirip gösteriyoruz
                        res.getSubPitch().getPitch().getName() + " - " + res.getSubPitch().getName(),
                        res.getBookingDate(),
                        res.getStartTime(),
                        res.getEndTime()
                ))
                .toList();
    }
}