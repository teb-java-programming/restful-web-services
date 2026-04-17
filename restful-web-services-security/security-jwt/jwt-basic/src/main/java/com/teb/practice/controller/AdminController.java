package com.teb.practice.controller;

import com.teb.practice.response.ApiResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @GetMapping("/home")
    public ApiResponse<String> adminHome() {

        return new ApiResponse<>("ADMIN access granted");
    }
}
