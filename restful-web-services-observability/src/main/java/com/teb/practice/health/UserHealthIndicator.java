package com.teb.practice.health;

import static org.springframework.boot.health.contributor.Health.down;
import static org.springframework.boot.health.contributor.Health.up;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class UserHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {

        if (checkApplication()) {
            return up().withDetail("service", "UP").withDetail("database", "NOT_CHECKED").build();
        }

        return down().withDetail("service", "DOWN").build();
    }

    private boolean checkApplication() {

        return true;
    }
}
