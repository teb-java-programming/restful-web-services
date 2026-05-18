package com.teb.practice.validator;

import static com.teb.practice.model.RideStatus.CANCELLED;
import static com.teb.practice.model.RideStatus.PAYMENT_COMPLETED;
import static com.teb.practice.model.RideStatus.REQUESTED;

import static java.util.Objects.isNull;

import com.teb.practice.event.RideEvent;
import com.teb.practice.model.Ride;
import com.teb.practice.model.RideStatus;

import org.springframework.stereotype.Component;

@Component
public class RideEventValidator {

    public boolean isValid(Ride ride, RideEvent event) {

        RideStatus newStatus = RideStatus.valueOf(event.getEventType().name());

        // Bootstrap rule
        if (isNull(ride)) return REQUESTED.equals(newStatus);

        RideStatus currentStatus = ride.getStatus();

        // Terminal state check
        if (CANCELLED.equals(currentStatus) || PAYMENT_COMPLETED.equals(currentStatus))
            return false;

        return currentStatus.canTransitionTo(newStatus);
    }
}
