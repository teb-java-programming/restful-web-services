package com.teb.practice.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record AuthorResponse(Long id, String name, String email, List<BookResponse> books) {}
