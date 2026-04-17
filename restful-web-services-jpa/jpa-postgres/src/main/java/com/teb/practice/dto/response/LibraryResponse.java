package com.teb.practice.dto.response;

import lombok.Builder;

@Builder
public record LibraryResponse(Long id, String name, String location, Long stock) {}
