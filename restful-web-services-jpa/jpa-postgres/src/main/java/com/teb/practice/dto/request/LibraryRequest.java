package com.teb.practice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LibraryRequest(@NotBlank String name, @NotBlank String location) {}
