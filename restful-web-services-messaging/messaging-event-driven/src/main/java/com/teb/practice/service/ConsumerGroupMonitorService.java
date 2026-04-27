package com.teb.practice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerGroupMonitorService {

    @Scheduled(fixedDelay = 15000)
    public void monitor() {

        log.info("Consumer group monitor active, checking partition assignments");
    }
}
