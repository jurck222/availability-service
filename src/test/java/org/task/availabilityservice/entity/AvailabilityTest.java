package org.task.availabilityservice.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AvailabilityTest {

    @Test
    public void testAvailabilityCreation() {
        int id = 1;
        int doctorId = 123;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(1);
        Boolean isBooked = false;

        Availability availability = new Availability(id, doctorId, startTime, endTime, isBooked);

        assertEquals(id, availability.getId());
        assertEquals(doctorId, availability.getDoctorId());
        assertEquals(startTime, availability.getStartTime());
        assertEquals(endTime, availability.getEndTime());
        assertEquals(isBooked, availability.getIsBooked());
    }

    @Test
    public void testAvailabilityNoArgsConstructor() {
        Availability availability = new Availability();

        assertNull(availability.getStartTime());
        assertNull(availability.getEndTime());
        assertNull(availability.getIsBooked());
    }

    @Test
    public void testSettersAndGetters() {
        Availability availability = new Availability();
        int doctorId = 456;
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(2);
        Boolean isBooked = true;

        availability.setDoctorId(doctorId);
        availability.setStartTime(startTime);
        availability.setEndTime(endTime);
        availability.setIsBooked(isBooked);

        assertEquals(doctorId, availability.getDoctorId());
        assertEquals(startTime, availability.getStartTime());
        assertEquals(endTime, availability.getEndTime());
        assertEquals(isBooked, availability.getIsBooked());
    }
}
