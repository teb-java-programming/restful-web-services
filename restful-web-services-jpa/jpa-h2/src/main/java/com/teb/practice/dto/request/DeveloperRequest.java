package com.teb.practice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DeveloperRequest(@NotBlank String name, @Email @NotBlank String email) {}
