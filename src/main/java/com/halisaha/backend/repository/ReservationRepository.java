package com.halisaha.backend.repository;

import com.halisaha.backend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    // DİKKAT: Artık PitchId değil, SubPitchId üzerinden kontrol yapıyoruz
    boolean existsBySubPitchIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
            Integer subPitchId,
            LocalDate bookingDate,
            LocalTime endTime,
            LocalTime startTime
    );

    List<Reservation> findAllByUser_Email(String email);
}