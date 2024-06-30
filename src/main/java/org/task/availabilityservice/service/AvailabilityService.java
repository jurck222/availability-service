package org.task.availabilityservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.task.availabilityservice.entity.Availability;
import org.task.availabilityservice.repository.AvailabilityRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AvailabilityService {
    private AvailabilityRepository availabilityRepository;

    public List<Availability> createAvailability(int doctorId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Availability> availabilities = new ArrayList<>();
        List<Availability> overlappingAvailabilities = availabilityRepository.findOverlappingAvailabilities(doctorId, startTime, endTime);

        if (!overlappingAvailabilities.isEmpty()) {
            throw new RuntimeException("Time window overlaps with existing availabilities");
        }

        while (startTime.isBefore(endTime)) {
            Availability availability = new Availability();
            availability.setDoctorId(doctorId);
            availability.setStartTime(startTime);
            availability.setEndTime(startTime.plusHours(1));
            availability.setIsBooked(false);
            availabilities.add(availability);
            startTime = startTime.plusHours(1);
        }

        return availabilityRepository.saveAll(availabilities);
    }

    public void deleteAvailability(int id) {
        if (availabilityRepository.existsById(id)) {
            availabilityRepository.deleteById(id);
        } else {
            throw new RuntimeException("Availability with id " + id + " does not exist");
        }
    }

    public List<Availability> getAvailabilityByDoctorId(int doctorId) {
        return new ArrayList<>(availabilityRepository.findByDoctorIdAndIsBookedFalse(doctorId));
    }

    public Availability getAvailabilityById(int id) {
        Optional<Availability> availability = availabilityRepository.findById(id);
        if (availability.isEmpty()) {
            throw new RuntimeException("Availability with id " + id + " does not exist");
        }
        return availability.get();
    }

    @Transactional
    public boolean markBooked(int availabilityId) {
        Availability availability = getAvailabilityById(availabilityId);
        if(!availability.getIsBooked()) {
            availability.setIsBooked(true);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean resetBooked(int availabilityId) {
        Availability availability = getAvailabilityById(availabilityId);
        if(availability.getIsBooked()) {
            availability.setIsBooked(false);
            return true;
        }
        return false;
    }
}
