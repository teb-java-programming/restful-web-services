package com.teb.practice.service;

import static java.time.LocalDateTime.now;

import com.teb.practice.event.RideEvent;
import com.teb.practice.event.RideEventType;
import com.teb.practice.exception.RideNotFoundException;
import com.teb.practice.model.Ride;
import com.teb.practice.model.RideEventEntity;
import com.teb.practice.model.RideStatus;
import com.teb.practice.repository.RideEventRepository;
import com.teb.practice.repository.RideRepository;
import com.teb.practice.util.JsonUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RideReplayService {

    private final RideEventRepository eventRepository;
    private final RideRepository rideRepository;
    private final JsonUtil jsonUtil;

    public Ride replay(String rideId) {

        rideRepository.deleteById(rideId);

        List<RideEvent> events =
                eventRepository.findByRideIdOrderByCreatedAtAsc(rideId).stream()
                        .map(this::toEvent)
                        .toList();

        if (events.isEmpty()) {
            throw new RideNotFoundException("No events found for rideId: " + rideId);
        }

        Ride ride =
                Ride.builder()
                        .rideId(rideId)
                        .userId(null)
                        .driverId(null)
                        .status(null)
                        .updatedAt(now())
                        .build();

        for (RideEvent event : events) {
            apply(ride, event);
        }

        return rideRepository.save(ride);
    }

    private void apply(Ride ride, RideEvent event) {

        ride.setUserId(event.getUserId());
        ride.setDriverId(event.getDriverId());
        ride.setStatus(map(event.getEventType()));
        ride.setUpdatedAt(now());
    }

    private RideStatus map(RideEventType type) {

        return RideStatus.valueOf(type.name());
    }

    private RideEvent toEvent(RideEventEntity entity) {

        return jsonUtil.fromJson(entity.getPayloadJson(), RideEvent.class);
    }
}
