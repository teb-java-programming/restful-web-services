package com.teb.practice.controller;

import com.teb.practice.response.ApiResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class UserController {

    @GetMapping("/home")
    public ApiResponse<String> userHome() {

        return new ApiResponse<>("USER access granted");
    }
}
