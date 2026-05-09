package com.teb.practice.repository;

import com.teb.practice.entity.EventStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventStatusRepository extends JpaRepository<EventStatus, Long> {

    Optional<EventStatus> findBySagaIdAndEventType(String sagaId, String eventType);
}
