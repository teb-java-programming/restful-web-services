package com.teb.practice;

import static org.springframework.boot.SpringApplication.run;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

// Exclusion added to prevent spring-boot look for DB
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SecurityBasicApp {

    public static void main(String[] args) {

        run(SecurityBasicApp.class, args);
    }
}
