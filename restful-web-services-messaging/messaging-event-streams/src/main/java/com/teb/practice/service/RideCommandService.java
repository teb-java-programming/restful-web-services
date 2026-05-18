package com.teb.practice.service;

import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;

import com.teb.practice.event.RideEvent;
import com.teb.practice.model.Ride;
import com.teb.practice.model.RideStatus;
import com.teb.practice.repository.RideRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideCommandService {

    private final RideRepository rideRepository;

    public void handle(RideEvent event) {

        RideStatus newStatus = RideStatus.valueOf(event.getEventType().name());

        Ride ride =
                rideRepository
                        .findById(event.getRideId())
                        .orElse(
                                Ride.builder()
                                        .rideId(event.getRideId())
                                        .userId(event.getUserId())
                                        .driverId(event.getDriverId())
                                        .status(newStatus)
                                        .build());

        if (isNull(event.getUserId())) event.setUserId(ride.getUserId());

        if (isNull(event.getDriverId())) event.setDriverId(ride.getDriverId());

        ride.setUserId(event.getUserId());
        ride.setDriverId(event.getDriverId());
        ride.setStatus(newStatus);
        ride.setUpdatedAt(now());

        rideRepository.save(ride);
    }
}
