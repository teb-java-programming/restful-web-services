package com.teb.practice.controller;

import com.teb.practice.model.Ride;
import com.teb.practice.service.RideReplayService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ride")
@RequiredArgsConstructor
public class RideReplayController {

    private final RideReplayService replayService;

    @PostMapping("/replay/{rideId}")
    public Ride replay(@PathVariable String rideId) {

        return replayService.replay(rideId);
    }
}
