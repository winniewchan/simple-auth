package com.winc.auth.service;

import com.winc.auth.entity.HashedPassword;
import com.winc.auth.entity.User;
import com.winc.auth.service.util.PasswordHasher;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private final Map<String, User> allUsers = new HashMap<>();

    private final PasswordHasher passwordHasher;

    public UserService(PasswordHasher passwordHasher) {
        this.passwordHasher = passwordHasher;
    }

    public User createUser(String username, String password) {
        if (exists(username)) {
            throw new IllegalArgumentException("User " + username + " already exists");
        }
        User user = new User(username, passwordHasher.hash(password));
        allUsers.put(username, user);
        return user;
    }

    public void deleteUser(User user) {
        if (!exists(user.getUsername())) {
            throw new IllegalArgumentException("User " + user.getUsername() + " does not exist");
        }
        allUsers.remove(user.getUsername());
    }

    User getUser(String username) {
        if (!exists(username)) {
            throw new IllegalArgumentException("User " + username + " does not exist");
        }
        return allUsers.get(username);
    }

    void authenticateUser(String username, String password) {
        User storedUser = getUser(username);
        HashedPassword storedPassword = storedUser.getHashedPassword();
        HashedPassword inputPassword = passwordHasher.hash(
                password,
                storedPassword.getSalt());
        if (!storedPassword.equals(inputPassword)) {
            throw new IllegalArgumentException("Invalid password for " + username);
        }
    }

    boolean exists(String username) {
        return allUsers.containsKey(username);
    }
}
