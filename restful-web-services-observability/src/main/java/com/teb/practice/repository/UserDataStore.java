package com.teb.practice.repository;

import static java.util.Comparator.comparing;

import com.teb.practice.exception.UserNotFoundException;
import com.teb.practice.model.User;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDataStore {

    private static final List<User> allUsers =
            new ArrayList<>(List.of(new User("Steve", 64), new User("John", 48)));

    public List<User> getUsers() {

        return allUsers.stream().sorted(comparing(User::getId)).toList();
    }

    public User getUser(String id) {

        return allUsers.stream()
                .filter(user -> user.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
