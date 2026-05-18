package com.teb.practice.service;

import static com.teb.practice.model.RideStatus.ASSIGNED;
import static com.teb.practice.model.RideStatus.CANCELLED;
import static com.teb.practice.model.RideStatus.PAYMENT_COMPLETED;
import static com.teb.practice.model.RideStatus.REQUESTED;

import static java.time.LocalDateTime.now;

import com.teb.practice.event.RideEvent;
import com.teb.practice.event.RideEventType;
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
                        .orElse(
                                Ride.builder()
                                        .rideId(event.getRideId())
                                        .userId(event.getUserId())
                                        .driverId(event.getDriverId())
                                        .build());

        // Terminal state check
        if (ride.getStatus() == CANCELLED || ride.getStatus() == PAYMENT_COMPLETED) {
            return;
        }

        // Cancellation allowed only before STARTED
        if (event.getEventType() == RideEventType.CANCELLED) {
            if (ride.getStatus() == REQUESTED || ride.getStatus() == ASSIGNED) {
                ride.setStatus(CANCELLED);
                ride.setUpdatedAt(now());

                rideRepository.save(ride);
            }

            return;
        }

        if (event.getUserId() == null) event.setUserId(ride.getUserId());

        if (event.getDriverId() == null) event.setDriverId(ride.getDriverId());

        ride.setUserId(event.getUserId());
        ride.setDriverId(event.getDriverId());
        ride.setStatus(map(event.getEventType()));
        ride.setUpdatedAt(now());

        rideRepository.save(ride);
    }

    private RideStatus map(RideEventType type) {

        return RideStatus.valueOf(type.name());
    }
}
