package com.teb.practice.dto.response;

import lombok.Builder;

@Builder
public record StockResponse(Long id, String bookTitle, String libraryName, Long quantity) {}
