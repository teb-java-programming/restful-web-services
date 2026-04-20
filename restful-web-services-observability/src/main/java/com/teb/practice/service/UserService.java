package com.teb.practice.service;

import com.teb.practice.model.User;
import com.teb.practice.repository.UserDataStore;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserDataStore userDataStore;

    public UserService(UserDataStore userRepository) {
        this.userDataStore = userRepository;
    }

    public List<User> getUsers() {

        return userDataStore.getUsers();
    }

    public User getUser(String id) {

        return userDataStore.getUser(id);
    }
}
