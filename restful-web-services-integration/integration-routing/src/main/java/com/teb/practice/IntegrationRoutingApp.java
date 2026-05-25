package com.teb.practice;

import static org.springframework.boot.SpringApplication.run;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class IntegrationRoutingApp {

    public static void main(String[] args) {

        run(IntegrationRoutingApp.class, args);
    }
}
