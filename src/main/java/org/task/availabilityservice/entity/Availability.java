package org.task.availabilityservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int doctorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isBooked;
}
