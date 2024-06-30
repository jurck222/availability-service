package org.task.availabilityservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.task.availabilityservice.entity.Availability;
import org.task.availabilityservice.service.AvailabilityService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AvailabilityController.class)
public class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvailabilityService availabilityService;

    @Test
    public void testCreateAvailability() throws Exception {
        int doctorId = 1;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(2);
        List<Availability> availabilities = List.of(new Availability());

        when(availabilityService.createAvailability(doctorId, startTime, endTime)).thenReturn(availabilities);

        mockMvc.perform(post("/api/v1/availability/{doctorId}", doctorId)
                        .param("startTime", startTime.toString())
                        .param("endTime", endTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(availabilities.size()));
    }

    @Test
    public void testGetAvailability() throws Exception {
        int doctorId = 1;
        List<Availability> availabilities = List.of(new Availability(), new Availability());

        when(availabilityService.getAvailabilityByDoctorId(doctorId)).thenReturn(availabilities);

        mockMvc.perform(get("/api/v1/availability/{doctorId}", doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(availabilities.size()));
    }

    @Test
    public void testDeleteAvailability() throws Exception {
        int availabilityId = 1;
        doNothing().when(availabilityService).deleteAvailability(availabilityId);

        mockMvc.perform(delete("/api/v1/availability/{availabilityId}", availabilityId))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAvailabilityById() throws Exception {
        int availabilityId = 1;
        Availability availability = new Availability();

        when(availabilityService.getAvailabilityById(availabilityId)).thenReturn(availability);

        mockMvc.perform(get("/api/v1/availability/info/{availabilityId}", availabilityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void testBookAvailability() throws Exception {
        int availabilityId = 1;
        when(availabilityService.markBooked(availabilityId)).thenReturn(true);

        mockMvc.perform(get("/api/v1/availability/book/{availabilityId}", availabilityId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testResetAvailability() throws Exception {
        int availabilityId = 1;
        when(availabilityService.resetBooked(availabilityId)).thenReturn(true);

        mockMvc.perform(get("/api/v1/availability/reset/{availabilityId}", availabilityId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}