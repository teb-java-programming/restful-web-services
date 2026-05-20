package com.teb.practice.service;

import com.teb.practice.event.RideEvent;
import com.teb.practice.repository.RideRepository;
import com.teb.practice.validator.RideEventValidator;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideFacadeService {

    private final RideRepository rideRepository;
    private final RideCommandService commandService;
    private final RideEventValidator eventValidator;
    private final RideEventStoreService eventStoreService;

    public boolean process(RideEvent event) {

        if (!eventValidator.isValid(
                rideRepository.findById(event.getRideId()).orElse(null), event)) {
            return false;
        }

        // Persist immutable event
        eventStoreService.store(event);
        // CQRS state projection update
        commandService.handle(event);

        return true;
    }
}
