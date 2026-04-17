package com.teb.practice.dto.request;

import jakarta.validation.constraints.NotNull;

public record StockRequest(Long bookId, Long libraryId, @NotNull Long quantity) {}
