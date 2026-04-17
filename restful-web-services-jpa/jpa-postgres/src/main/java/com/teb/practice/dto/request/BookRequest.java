package com.teb.practice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BookRequest(@NotBlank String title, Long authorId) {}
