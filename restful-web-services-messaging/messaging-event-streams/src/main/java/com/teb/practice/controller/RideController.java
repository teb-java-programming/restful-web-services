package com.teb.practice.controller;

import static com.teb.practice.event.RideEventType.REQUESTED;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

import com.teb.practice.event.RideEvent;
import com.teb.practice.service.RideCommandService;
import com.teb.practice.service.RideEventStoreService;

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

    private final RideCommandService commandService;
    private final RideEventStoreService eventStoreService;

    @PostMapping("/request")
    public Map<String, String> request(@RequestBody Map<String, String> request) {

        String rideId = randomUUID().toString();

        RideEvent event =
                RideEvent.builder()
                        .rideId(rideId)
                        .userId(request.get("userId"))
                        .driverId(null)
                        .eventType(REQUESTED)
                        .timestamp(now())
                        .build();

        // Persist immutable event
        eventStoreService.store(event);

        // CQRS state projection update
        commandService.handle(event);

        return Map.of("rideId", rideId, "status", "REQUESTED");
    }

    @PostMapping("/emit")
    public Map<String, String> emit(@RequestBody RideEvent event) {

        eventStoreService.store(event);
        commandService.handle(event);

        return Map.of(
                "rideId", event.getRideId(),
                "status", event.getEventType().name());
    }
}
