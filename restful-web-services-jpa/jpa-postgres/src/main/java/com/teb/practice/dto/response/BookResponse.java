package com.teb.practice.dto.response;

import lombok.Builder;

@Builder
public record BookResponse(Long id, String title, String isbn, Long authorId) {}
