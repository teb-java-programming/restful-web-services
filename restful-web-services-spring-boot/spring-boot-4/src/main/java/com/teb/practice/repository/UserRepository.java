package com.teb.practice.repository;

import static java.util.Comparator.comparing;

import com.teb.practice.exception.UserExistsException;
import com.teb.practice.exception.UserNotFoundException;
import com.teb.practice.model.User;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    private static final List<User> allUsers =
            new ArrayList<>(
                    List.of(
                            new User("U01", "Steve", "austin316@test.com"),
                            new User("U02", "John", "ucantcme@test.com")));

    public List<User> getUsers() {

        return allUsers.stream().sorted(comparing(User::id)).toList();
    }

    public User getUser(String id) {

        return findUser(id);
    }

    public User createUser(User user) {

        if (allUsers.stream().anyMatch(u -> u.id().equals(user.id())))
            throw new UserExistsException("User already exists");

        allUsers.add(user);

        return user;
    }

    public User updateUser(String id, User user) {

        if (allUsers.stream().noneMatch(u -> u.id().equals(id)))
            throw new UserNotFoundException("User not found");

        allUsers.removeIf(u -> u.id().equals(id));
        allUsers.add(user);

        return user;
    }

    public User patchUser(String id, User user) {

        User existingUser = findUser(id);
        User updatedUser =
                new User(
                        existingUser.id(),
                        user.name() != null ? user.name() : existingUser.name(),
                        user.email() != null ? user.email() : existingUser.email());

        allUsers.removeIf(u -> u.id().equals(id));
        allUsers.add(updatedUser);

        return updatedUser;
    }

    private User findUser(String id) {

        return allUsers.stream()
                .filter(user -> user.id().equals(id))
                .findAny()
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
