package com.teb.practice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideEvent {

    private String rideId;
    private String userId;
    private String driverId;
    private RideEventType eventType;
    private LocalDateTime timestamp;
}
