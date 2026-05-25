package com.teb.practice;

import static org.springframework.boot.SpringApplication.run;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Slf4j
public class IntegrationObservabilityApp {

    public static void main(String[] args) {

        run(IntegrationObservabilityApp.class, args);
    }

    @PostConstruct
    public void check() {
        log.info("JMS routes starting...");
    }
}
