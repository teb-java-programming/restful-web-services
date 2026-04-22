package com.teb.practice.controller;

import com.teb.practice.dto.PublicResponse;
import com.teb.practice.metrics.UserMetricsService;
import com.teb.practice.model.User;
import com.teb.practice.repository.UserDataStore;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Public User API", description = "User management operations without authentication")
public class PublicController {

    private final UserDataStore userDataStore;
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
                                        schema = @Schema(implementation = PublicResponse.class)))
            })
    public List<PublicResponse> getUsers() {

        List<User> users = userDataStore.getUsers();

        userMetricsService.incrementApiCounter();

        log.debug("List of users: {}", users);

        return users.stream().map(user -> new PublicResponse(user.getId())).toList();
    }
}
