package org.task.availabilityservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.task.availabilityservice.entity.Availability;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Integer> {
    List<Availability> findByDoctorIdAndIsBookedFalse(int doctorId);

    @Query("SELECT a FROM Availability a WHERE a.doctorId = :doctorId AND " +
            "((a.startTime < :endTime AND a.endTime > :startTime) OR " +
            "(a.startTime >= :startTime AND a.startTime < :endTime) OR " +
            "(a.endTime > :startTime AND a.endTime <= :endTime))")
    List<Availability> findOverlappingAvailabilities(@Param("doctorId") int doctorId,
                                                     @Param("startTime") LocalDateTime startTime,
                                                     @Param("endTime") LocalDateTime endTime);

    Optional<Availability> findByStartTimeAndEndTime(LocalDateTime startTime, LocalDateTime endTime);
}

