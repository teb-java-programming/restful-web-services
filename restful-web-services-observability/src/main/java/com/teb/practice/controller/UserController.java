package com.teb.practice.controller;

import com.teb.practice.exception.ErrorResponse;
import com.teb.practice.metrics.UserMetricsService;
import com.teb.practice.model.User;
import com.teb.practice.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "User API", description = "User management operations")
public class UserController {

    private final UserService userService;
    private final UserMetricsService userMetricsService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns list of all users")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Users retrieved",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = User.class)))
            })
    public List<User> getUsers() {

        List<User> users = userService.getUsers();

        userMetricsService.incrementApiCounter();

        log.debug("List of users: {}", users);

        return users;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "Returns a user using unique identifier")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "User retrieved",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = User.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "User not found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public User getUser(@Valid @PathVariable String id) {

        User user = userService.getUser(id);

        userMetricsService.incrementApiCounter();

        log.debug("User: {}", user);

        return user;
    }
}
