package com.teb.practice.config;

import static org.slf4j.MDC.clear;
import static org.slf4j.MDC.put;

import static java.util.UUID.randomUUID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID = "correlationId";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String correlationId = request.getHeader(CORRELATION_ID);

            if (correlationId == null) {
                correlationId = randomUUID().toString();
            }

            put(CORRELATION_ID, correlationId);
            response.setHeader(CORRELATION_ID, correlationId);

            log.debug("Correlation ID set: {}", correlationId);

            filterChain.doFilter(request, response);
        } finally {
            clear();
        }
    }
}
