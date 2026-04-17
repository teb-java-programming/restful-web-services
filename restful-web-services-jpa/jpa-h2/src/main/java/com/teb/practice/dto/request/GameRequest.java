package com.teb.practice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GameRequest(@NotBlank String title, @NotBlank String genre, Long developerId) {}
