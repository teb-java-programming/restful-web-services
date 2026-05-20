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

        Ride ride =
                rideRepository
                        .findById(event.getRideId())
                        .orElseGet(() -> Ride.builder().rideId(event.getRideId()).build());

        ride.setUserId(coalesce(event.getUserId(), ride.getUserId()));
        ride.setDriverId(coalesce(event.getDriverId(), ride.getDriverId()));
        ride.setStatus(RideStatus.valueOf(event.getEventType().name()));
        ride.setUpdatedAt(now());

        rideRepository.save(ride);
    }

    private <T> T coalesce(T actual, T expected) {

        return isNull(actual) ? expected : actual;
    }
}
