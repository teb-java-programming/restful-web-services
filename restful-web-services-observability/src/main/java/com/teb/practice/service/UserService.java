package com.teb.practice.service;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import com.teb.practice.dto.SecureResponse;
import com.teb.practice.model.User;
import com.teb.practice.repository.UserDataStore;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserService {

    private final UserDataStore userDataStore;

    public UserService(UserDataStore userRepository) {
        this.userDataStore = userRepository;
    }

    public List<SecureResponse> getUsers() {

        List<User> users = userDataStore.getUsers();

        if (isAdmin(getContext().getAuthentication())) {
            return users.stream()
                    .map(user -> new SecureResponse(user.getId(), user.getName(), user.getAge()))
                    .toList();
        }

        return users.stream()
                .map(user -> new SecureResponse(user.getId(), user.getName(), null))
                .toList();
    }

    public SecureResponse getUser(String id) {

        User user = userDataStore.getUser(id);

        if (isAdmin(getContext().getAuthentication())) {
            return new SecureResponse(user.getId(), user.getName(), user.getAge());
        }

        return new SecureResponse(user.getId(), user.getName(), null);
    }

    private boolean isAdmin(Authentication authentication) {

        return authentication != null
                && authentication.getAuthorities().stream()
                        .anyMatch(auth -> Objects.equals(auth.getAuthority(), "ROLE_ADMIN"));
    }
}
