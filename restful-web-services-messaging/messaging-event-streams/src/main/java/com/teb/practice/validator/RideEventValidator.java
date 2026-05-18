package com.teb.practice.validator;

import static com.teb.practice.model.RideStatus.ASSIGNED;
import static com.teb.practice.model.RideStatus.CANCELLED;
import static com.teb.practice.model.RideStatus.COMPLETED;
import static com.teb.practice.model.RideStatus.PAYMENT_COMPLETED;
import static com.teb.practice.model.RideStatus.REQUESTED;
import static com.teb.practice.model.RideStatus.STARTED;

import static java.util.Objects.isNull;

import com.teb.practice.event.RideEvent;
import com.teb.practice.model.Ride;
import com.teb.practice.model.RideStatus;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class RideEventValidator {

    private static final Map<RideStatus, Set<RideStatus>> VALID_TRANSITIONS =
            Map.of(
                    REQUESTED,
                    Set.of(ASSIGNED, CANCELLED),
                    ASSIGNED,
                    Set.of(STARTED, CANCELLED),
                    STARTED,
                    Set.of(COMPLETED),
                    COMPLETED,
                    Set.of(PAYMENT_COMPLETED));

    public boolean isValid(Ride ride, RideEvent event) {

        RideStatus newStatus = RideStatus.valueOf(event.getEventType().name());

        // Bootstrap rule
        if (isNull(ride)) return REQUESTED.equals(newStatus);

        RideStatus currentStatus = ride.getStatus();

        // Terminal state check
        if (CANCELLED.equals(currentStatus) || PAYMENT_COMPLETED.equals(currentStatus))
            return false;

        return VALID_TRANSITIONS.getOrDefault(currentStatus, Set.of()).contains(newStatus);
    }
}
