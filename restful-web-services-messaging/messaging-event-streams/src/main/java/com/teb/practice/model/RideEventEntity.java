package com.teb.practice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ride_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideEventEntity {

    @Id
    @Column(name = "event_id", length = 64)
    private String eventId;

    @Column(name = "ride_id", nullable = false)
    private String rideId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "payload_json", nullable = false, columnDefinition = "text")
    private String payloadJson;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
