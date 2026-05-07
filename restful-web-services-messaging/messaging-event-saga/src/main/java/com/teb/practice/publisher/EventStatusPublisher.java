package com.teb.practice.publisher;

import com.teb.practice.entity.EventStatus;
import com.teb.practice.repository.EventStatusRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventStatusPublisher {

    private final EventStatusRepository eventStatusRepository;

    @Scheduled(fixedDelay = 1000)
    public void publishPendingEvents() {

        List<EventStatus> pendingEvents = eventStatusRepository.findByStatus("PENDING");

        for (EventStatus event : pendingEvents) {
            event.setStatus("SENT");
            eventStatusRepository.save(event);
        }
    }
}
