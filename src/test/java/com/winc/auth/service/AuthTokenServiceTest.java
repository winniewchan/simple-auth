package com.winc.auth.service;

import com.winc.auth.entity.Role;
import com.winc.auth.entity.User;
import com.winc.auth.service.util.AuthClock;
import com.winc.auth.service.util.TokenGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthTokenServiceTest {

    private static final String username = "username";
    private static final String password = "password";
    private static final String token = "token";
    private static final Role role = new Role("role");

    @Mock
    UserService userService;
    @Mock
    TokenGenerator tokenGenerator;
    @Mock
    AuthClock authClock;

    @InjectMocks
    AuthTokenService authTokenService;

    @Test
    void authenticate_fails_if_user_authentication_fails() {
        doThrow(new IllegalArgumentException("User cannot be authenticated"))
                .when(userService).authenticateUser(username, password);

        assertThrows(
                IllegalArgumentException.class,
                () -> authTokenService.authenticate(username, password));
    }

    @Test
    void authenticate_fails_if_generated_token_not_unique() {
        authenticate();

        Exception e = assertThrows(
                RuntimeException.class,
                () -> authTokenService.authenticate("anotherUser", "generatesDuplicatedToken"));
        assertEquals("Failed to generate a new unique token, please try again", e.getMessage());
    }

    @Test
    void authenticate_returns_auth_token_valid_for_2_hours() {
        LocalDateTime twoHoursFromNow = LocalDateTime.parse("2021-09-01T12:15:30");
        when(authClock.twoHoursFromNow()).thenReturn(twoHoursFromNow);

        String generatedToken = authenticate();

        assertEquals(token, generatedToken);
        assertEquals(username, authTokenService.getToken(generatedToken).getUsername());
        assertEquals(twoHoursFromNow, authTokenService.getToken(generatedToken).getExpiry());
    }

    @Test
    void invalidate_auth_token() {
        String generatedToken = authenticate();

        authTokenService.invalidate(generatedToken);

        assertFalse(authTokenService.exists(generatedToken));
    }

    @Test
    void invalidate_auth_token_ignores_invalid_token() {
        String invalidToken = "invalidTokenDoesNotExists";

        authTokenService.invalidate("invalidTokenDoesNotExists");

        assertFalse(authTokenService.exists(invalidToken));
    }

    @Test
    void check_roles_fails_if_token_invalid() {
        assertThrows(
                IllegalArgumentException.class,
                () -> authTokenService.checkRole(token, role));
        assertThrows(
                IllegalArgumentException.class,
                () -> authTokenService.allRoles(token));
    }

    @Test
    void check_role_fails_if_token_expired() {
        String generatedToken = authenticate();
        whenTokenHasExpired(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> authTokenService.checkRole(generatedToken, role));
        assertThrows(
                IllegalArgumentException.class,
                () -> authTokenService.allRoles(generatedToken));
    }

    @Test
    void check_role_fails_if_token_valid_but_user_no_longer_exists() {
        String generatedToken = authenticate();
        whenTokenHasExpired(false);

        when(userService.getUser(username))
                .thenThrow(new RuntimeException("User does not exist"));

        assertThrows(
                RuntimeException.class,
                () -> authTokenService.checkRole(generatedToken, role));
        assertThrows(
                RuntimeException.class,
                () -> authTokenService.allRoles(generatedToken));
    }

    @Test
    void check_role_if_token_valid_for_user() {
        String generatedToken = authenticate();
        whenTokenHasExpired(false);

        User user = new User(username, null);
        user.addRole(role);
        when(userService.getUser(username)).thenReturn(user);

        assertTrue(authTokenService.checkRole(generatedToken, role));
        assertFalse(authTokenService.checkRole(generatedToken, new Role("notMyRole")));
        assertEquals(singleton(role), authTokenService.allRoles(generatedToken));
    }

    private String authenticate() {
        when(tokenGenerator.generate()).thenReturn(token);
        return authTokenService.authenticate(username, password);
    }

    private void whenTokenHasExpired(boolean expired) {
        when(authClock.isExpired(any())).thenReturn(expired);
    }

}