package com.winc.auth.entity;

import java.util.HashSet;
import java.util.Set;

public class User {

    private final String username;
    private final HashedPassword hashedPassword;
    private final Set<Role> roles = new HashSet<>();

    public User(String username, HashedPassword hashedPassword) {
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public String getUsername() {
        return username;
    }

    public HashedPassword getHashedPassword() {
        return hashedPassword;
    }

    public Set<Role> getRoles() {
        return new HashSet<>(roles);
    }

}
