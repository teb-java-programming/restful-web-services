package com.teb.practice.model;

import static jakarta.persistence.EnumType.STRING;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ride")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride {

    @Id
    @Column(name = "ride_id", length = 64)
    private String rideId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "driver_id")
    private String driverId;

    @Enumerated(STRING)
    @Column(name = "status", nullable = false)
    private RideStatus status;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
