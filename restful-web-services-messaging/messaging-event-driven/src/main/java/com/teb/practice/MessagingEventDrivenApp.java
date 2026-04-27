package com.teb.practice;

import static org.springframework.boot.SpringApplication.run;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MessagingEventDrivenApp {

    public static void main(String[] args) {

        run(MessagingEventDrivenApp.class, args);
    }
}
