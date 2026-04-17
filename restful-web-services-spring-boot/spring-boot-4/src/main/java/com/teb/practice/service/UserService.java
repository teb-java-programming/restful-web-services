package com.teb.practice.service;

import com.teb.practice.model.User;
import com.teb.practice.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {

        return userRepository.getUsers();
    }

    public User getUser(String id) {

        return userRepository.getUser(id);
    }

    public User createUser(User user) {

        return userRepository.createUser(user);
    }

    public User updateUser(String id, User user) {

        return userRepository.updateUser(id, user);
    }

    public User patchUser(String id, User user) {

        return userRepository.patchUser(id, user);
    }
}
