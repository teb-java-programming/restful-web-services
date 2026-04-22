package com.teb.practice.security;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import com.teb.practice.token.TokenBlacklist;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenBlacklist tokenBlacklist;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, TokenBlacklist tokenBlacklist) {
        this.jwtUtil = jwtUtil;
        this.tokenBlacklist = tokenBlacklist;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            // Blacklist token first
            if (tokenBlacklist.isRevoked(token)) {
                response.sendError(SC_UNAUTHORIZED);

                return;
            }

            if (jwtUtil.isValid(token)) {
                getContext()
                        .setAuthentication(
                                new UsernamePasswordAuthenticationToken(
                                        jwtUtil.extractUsername(token),
                                        null,
                                        jwtUtil.extractRoles(token).stream()
                                                .map(SimpleGrantedAuthority::new)
                                                .toList()));
            }
        }

        filterChain.doFilter(request, response);
    }
}
