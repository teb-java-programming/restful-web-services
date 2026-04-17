package com.teb.practice.security;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import static org.springframework.security.core.context.SecurityContextHolder.clearContext;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import com.teb.practice.token.RevokeTokenService;
import com.teb.practice.user.User;
import com.teb.practice.user.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RevokeTokenService revokeTokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        clearContext();

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);

            return;
        }

        String token = header.substring(7);

        if (!jwtService.isValid(token)) {
            response.setStatus(SC_UNAUTHORIZED);

            return;
        }

        var claims = jwtService.extractClaims(token);

        if (revokeTokenService.isRevoked(jwtService.extractJti(token))) {
            response.setStatus(SC_UNAUTHORIZED);

            return;
        }

        User user = userRepository.findByUsername(claims.getSubject()).orElse(null);

        if (user == null) {
            response.setStatus(SC_UNAUTHORIZED);

            return;
        }

        if (!user.getTokenVersion().equals(((Number) claims.get("ver")).longValue())) {
            response.setStatus(SC_UNAUTHORIZED);

            return;
        }

        getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getRoles().stream()
                                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                        .toList()));

        filterChain.doFilter(request, response);
    }
}
