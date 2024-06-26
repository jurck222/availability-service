package org.task.availabilityservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.task.availabilityservice.entity.Availability;
import org.task.availabilityservice.service.AvailabilityService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/availability")
@AllArgsConstructor
public class AvailabilityController {
    private AvailabilityService availabilityService;

    @PostMapping("/{doctorId}")
    public List<Availability> createAvailability(@PathVariable int doctorId, @RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime) {
        return availabilityService.createAvailability(doctorId, startTime, endTime);
    }

    @GetMapping("/{doctorId}")
    public List<Availability> getAvailability(@PathVariable int doctorId) {
        return availabilityService.getAvailabilityByDoctorId(doctorId);
    }

    @DeleteMapping("/{availabilityId}")
    public void deleteAvailability(@PathVariable int availabilityId) {
        availabilityService.deleteAvailability(availabilityId);
    }

    @GetMapping("/info/{availabilityId}")
    public Availability getAvailabilityById(@PathVariable int availabilityId) {
        return availabilityService.getAvailabilityById(availabilityId);
    }

    @GetMapping("/book/{availabilityId}")
    public boolean bookAvailability(@PathVariable int availabilityId) {
        return availabilityService.markBooked(availabilityId);
    }

    @GetMapping("/reset/{availabilityId}")
    public boolean resetAvailability(@PathVariable int availabilityId) {
        return availabilityService.resetBooked(availabilityId);
    }
}

