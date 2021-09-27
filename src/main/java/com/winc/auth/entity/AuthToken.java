package com.winc.auth.entity;

import java.time.LocalDateTime;

public class AuthToken {

    private final String username;
    private final LocalDateTime expiry;

    public AuthToken(String username, LocalDateTime expiry) {
        this.username = username;
        this.expiry = expiry;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }
}
