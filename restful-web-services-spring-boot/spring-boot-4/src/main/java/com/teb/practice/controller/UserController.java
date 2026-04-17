package com.teb.practice.controller;

import static org.springframework.http.ResponseEntity.status;

import com.teb.practice.model.User;
import com.teb.practice.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {

        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {

        return userService.getUser(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody User user) {

        return status(201)
                .body(new ApiResponse<>(userService.createUser(user), "User created"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable String id, @RequestBody User user) {

        return ResponseEntity.ok(
                new ApiResponse<>(userService.updateUser(id, user), "User updated"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> patchUser(
            @PathVariable String id, @RequestBody User user) {

        return ResponseEntity.ok(
                new ApiResponse<>(userService.patchUser(id, user), "User patched"));
    }
}
