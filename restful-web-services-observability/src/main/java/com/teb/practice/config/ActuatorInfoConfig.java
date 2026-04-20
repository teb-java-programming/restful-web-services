package com.teb.practice.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ActuatorInfoConfig implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {

        builder.withDetails(
                Map.of(
                        "module",
                        "restful-web-services-observability",
                        "topics",
                        List.of("Actuator", "Metrics", "Health", "Logging")));
    }
}
