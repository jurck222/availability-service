package org.task.availabilityservice.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.task.availabilityservice.entity.Availability;
import org.task.availabilityservice.repository.AvailabilityRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AvailabilityServiceTest {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @InjectMocks
    private AvailabilityService availabilityService;

    public AvailabilityServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAvailability_WithoutOverlap() {
        int doctorId = 1;
        LocalDateTime startTime = LocalDateTime.of(2024, 7, 3, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 7, 3, 13, 0);

        when(availabilityRepository.findOverlappingAvailabilities(doctorId, startTime, endTime))
                .thenReturn(new ArrayList<>());

        List<Availability> expectedAvailabilities = new ArrayList<>();
        LocalDateTime currentStartTime = startTime;
        while (currentStartTime.isBefore(endTime)) {
            Availability availability = new Availability();
            availability.setDoctorId(doctorId);
            availability.setStartTime(currentStartTime);
            availability.setEndTime(currentStartTime.plusHours(1));
            availability.setIsBooked(false);
            expectedAvailabilities.add(availability);
            currentStartTime = currentStartTime.plusHours(1);
        }

        when(availabilityRepository.saveAll(expectedAvailabilities))
                .thenReturn(expectedAvailabilities);

        List<Availability> createdAvailabilities = availabilityService.createAvailability(doctorId, startTime, endTime);

        assertEquals(expectedAvailabilities.size(), createdAvailabilities.size());
        assertFalse(createdAvailabilities.isEmpty());
        assertFalse(createdAvailabilities.get(0).getIsBooked());
        verify(availabilityRepository, times(1)).saveAll(expectedAvailabilities);
    }

    @Test
    public void testCreateAvailability_WithOverlap() {
        int doctorId = 1;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(2);
        List<Availability> overlapping = List.of(new Availability());
        when(availabilityRepository.findOverlappingAvailabilities(doctorId, startTime, endTime))
                .thenReturn(overlapping);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            availabilityService.createAvailability(doctorId, startTime, endTime);
        });

        assertEquals("Time window overlaps with existing availabilities", exception.getMessage());
    }

    @Test
    public void testDeleteAvailability_Exists() {
        int id = 1;
        when(availabilityRepository.existsById(id)).thenReturn(true);

        availabilityService.deleteAvailability(id);

        verify(availabilityRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteAvailability_NotExists() {
        int id = 1;
        when(availabilityRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            availabilityService.deleteAvailability(id);
        });

        assertEquals("Availability with id 1 does not exist", exception.getMessage());
    }

    @Test
    public void testGetAvailabilityByDoctorId() {
        int doctorId = 1;
        List<Availability> availabilities = List.of(new Availability(), new Availability());
        when(availabilityRepository.findByDoctorIdAndIsBookedFalse(doctorId)).thenReturn(availabilities);

        List<Availability> result = availabilityService.getAvailabilityByDoctorId(doctorId);

        assertEquals(2, result.size());
    }

    @Test
    public void testGetAvailabilityById_Exists() {
        int id = 1;
        Availability availability = new Availability();
        when(availabilityRepository.findById(id)).thenReturn(Optional.of(availability));

        Availability result = availabilityService.getAvailabilityById(id);

        assertNotNull(result);
    }

    @Test
    public void testGetAvailabilityById_NotExists() {
        int id = 1;
        when(availabilityRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            availabilityService.getAvailabilityById(id);
        });

        assertEquals("Availability with id 1 does not exist", exception.getMessage());
    }

    @Test
    public void testMarkBooked_NotBooked() {
        int id = 1;
        Availability availability = new Availability();
        availability.setIsBooked(false);
        when(availabilityRepository.findById(id)).thenReturn(Optional.of(availability));

        boolean result = availabilityService.markBooked(id);

        assertTrue(result);
        assertTrue(availability.getIsBooked());
    }

    @Test
    public void testResetBooked_Booked() {
        int id = 1;
        Availability availability = new Availability();
        availability.setIsBooked(true);
        when(availabilityRepository.findById(id)).thenReturn(Optional.of(availability));

        boolean result = availabilityService.resetBooked(id);

        assertTrue(result);
        assertFalse(availability.getIsBooked());
    }
}