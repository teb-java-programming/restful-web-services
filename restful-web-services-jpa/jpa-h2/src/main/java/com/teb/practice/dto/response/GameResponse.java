package com.teb.practice.dto.response;

import lombok.Builder;

@Builder
public record GameResponse(Long id, String title, String genre, Long developerId) {}
