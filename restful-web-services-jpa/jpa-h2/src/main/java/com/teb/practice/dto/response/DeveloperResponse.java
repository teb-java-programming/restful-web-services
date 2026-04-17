package com.teb.practice.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record DeveloperResponse(Long id, String name, String email, List<GameResponse> games) {}
