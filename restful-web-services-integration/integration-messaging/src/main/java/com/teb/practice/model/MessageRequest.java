package com.teb.practice.model;

import jakarta.validation.constraints.NotBlank;

public record MessageRequest(@NotBlank(message = "message must not be blank") String message) {}
