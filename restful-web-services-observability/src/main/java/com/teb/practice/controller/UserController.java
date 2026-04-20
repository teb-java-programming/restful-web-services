package com.teb.practice.controller;

import com.teb.practice.metrics.UserMetricsService;
import com.teb.practice.model.User;
import com.teb.practice.service.UserService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserMetricsService userMetricsService;

    @GetMapping
    public List<User> getUsers() {

        List<User> users = userService.getUsers();

        userMetricsService.incrementApiCounter();

        log.debug("List of users: {}", users);

        return users;
    }

    @GetMapping("/{id}")
    public User getUser(@Valid @PathVariable String id) {

        User user = userService.getUser(id);

        userMetricsService.incrementApiCounter();

        log.debug("User: {}", user);

        return user;
    }
}
