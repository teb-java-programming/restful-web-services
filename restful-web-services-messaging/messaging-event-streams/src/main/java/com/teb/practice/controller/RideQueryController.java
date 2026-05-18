package com.teb.practice.controller;

import com.teb.practice.exception.RideNotFoundException;
import com.teb.practice.model.Ride;
import com.teb.practice.repository.RideRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ride")
@RequiredArgsConstructor
public class RideQueryController {

    private final RideRepository repository;

    @GetMapping("/{rideId}")
    public Ride getRide(@PathVariable String rideId) {

        return repository
                .findById(rideId)
                .orElseThrow(() -> new RideNotFoundException("Ride not found: " + rideId));
    }
}
