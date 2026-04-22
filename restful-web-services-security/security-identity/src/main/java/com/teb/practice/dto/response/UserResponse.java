package com.teb.practice.dto.response;

public record UserResponse(Long id, String username, String email, String role, boolean enabled) {}
