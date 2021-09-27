package com.winc.auth.service;

import com.winc.auth.entity.AuthToken;
import com.winc.auth.entity.Role;
import com.winc.auth.service.util.AuthClock;
import com.winc.auth.service.util.TokenGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AuthTokenService {

    private final Map<String, AuthToken> allTokens = new HashMap<>();

    private final UserService userService;
    private final TokenGenerator tokenGenerator;
    private final AuthClock authClock;

    public AuthTokenService(UserService userService, TokenGenerator tokenGenerator, AuthClock authClock) {
        this.userService = userService;
        this.tokenGenerator = tokenGenerator;
        this.authClock = authClock;
    }

    public String authenticate(String username, String password) {
        userService.authenticateUser(username, password);

        String token = uniqueToken();
        allTokens.put(
                token,
                new AuthToken(username, authClock.twoHoursFromNow()));

        return token;
    }

    public void invalidate(String token) {
        allTokens.remove(token);
    }

    public boolean checkRole(String token, Role role) {
        Set<Role> roles = allRoles(token);
        return roles.contains(role);
    }

    public Set<Role> allRoles(String token) {
        AuthToken authToken = getToken(token);
        if (authClock.isExpired(authToken)) {
            throw new IllegalArgumentException("Token already expired at " + authToken.getExpiry());
        }
        return userService
                .getUser(authToken.getUsername())
                .getRoles();
    }

    AuthToken getToken(String token) {
        if (!exists(token)) {
            throw new IllegalArgumentException("Invalid token " + token);
        }
        return allTokens.get(token);
    }

    boolean exists(String token) {
        return allTokens.containsKey(token);
    }

    private String uniqueToken() {
        String token = tokenGenerator.generate();
        if (exists(token)) {
            throw new RuntimeException("Failed to generate a new unique token, please try again");
        }
        return token;
    }

}
