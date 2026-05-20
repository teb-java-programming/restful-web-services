package com.teb.practice.controller;

import static com.teb.practice.event.RideEventType.ASSIGNED;
import static com.teb.practice.event.RideEventType.REQUESTED;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

import com.teb.practice.event.RideEvent;
import com.teb.practice.service.RideFacadeService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/ride")
@RequiredArgsConstructor
public class RideController {

    private static final String RIDE_ID = "rideId";
    private static final String USER_ID = "userId";
    private static final String STATUS = "status";

    private final RideFacadeService rideFacadeService;

    @PostMapping("/request")
    public Map<String, String> request(@RequestBody Map<String, String> request) {

        String rideId = randomUUID().toString();
        String userId = request.get(USER_ID);

        RideEvent event =
                RideEvent.builder()
                        .rideId(rideId)
                        .userId(userId)
                        .driverId(null)
                        .eventType(REQUESTED)
                        .timestamp(now())
                        .build();

        rideFacadeService.process(event);

        return Map.of(RIDE_ID, rideId, USER_ID, userId, STATUS, REQUESTED.name());
    }

    @PostMapping("/update")
    public Map<String, String> update(@RequestBody RideEvent event) {

        if (!rideFacadeService.process(event)) {
            return Map.of(STATUS, "INVALID_EVENT");
        }

        if (ASSIGNED.equals(event.getEventType())) {
            return Map.of(
                    RIDE_ID,
                    event.getRideId(),
                    "driverId",
                    event.getDriverId(),
                    STATUS,
                    event.getEventType().name());
        }

        return Map.of(
                RIDE_ID, event.getRideId(),
                STATUS, event.getEventType().name());
    }
}
