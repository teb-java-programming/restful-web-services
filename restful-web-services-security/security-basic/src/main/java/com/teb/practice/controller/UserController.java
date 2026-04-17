package com.teb.practice.controller;

import static org.springframework.http.ResponseEntity.ok;

import com.teb.practice.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Operation(summary = "User API", description = "Basic authentication required")
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test() {

        return ok(new ApiResponse<>("User endpoint test"));
    }
}
