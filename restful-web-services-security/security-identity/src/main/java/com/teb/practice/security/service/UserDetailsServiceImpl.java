package com.teb.practice.security.service;

import static org.springframework.security.core.userdetails.User.withUsername;

import com.teb.practice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String email)
            throws UsernameNotFoundException {

        return userRepository
                .findByEmail(email)
                .map(
                        user ->
                                withUsername(user.getEmail())
                                        .password(user.getPassword())
                                        .disabled(!user.isEnabled())
                                        .authorities(user.getRole())
                                        .build())
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
