package com.github.rayinfinite.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SchedulerApplication {

    public static void main(String[] args) {
        System.setProperty("ical4j.parsing.relaxed", "true");
        SpringApplication.run(SchedulerApplication.class, args);
    }
}
