package com.teb.practice.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record User(@NotBlank String id, @NotBlank String name, @Email @NotBlank String email) {}
