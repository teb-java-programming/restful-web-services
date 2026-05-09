package com.teb.practice;

import static org.springframework.boot.SpringApplication.run;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MessagingEventSagaApp {

    public static void main(String[] args) {

        run(MessagingEventSagaApp.class, args);
    }
}
