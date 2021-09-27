package com.winc.auth.service;

import com.winc.auth.entity.HashedPassword;
import com.winc.auth.entity.User;
import com.winc.auth.service.util.PasswordHasher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String smith = "smith";
    private static final String password = "password";
    public static final byte[] hashedPassword = "hashedPassword".getBytes(UTF_8);
    private static final byte[] salt = "salt".getBytes(UTF_8);

    @Mock
    PasswordHasher passwordHasher;

    @InjectMocks
    UserService userService;

    @Test
    void create_user_fails_if_user_already_exists() {
        userService.createUser(smith, password);

        Exception e = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(smith, "sameUsernameDifferentPassword"));
        assertEquals("User smith already exists", e.getMessage());
    }

    @Test
    void create_user_with_hashed_password_and_no_roles_by_default() {
        when(passwordHasher.hash(password))
                .thenReturn(new HashedPassword(hashedPassword, salt));

        User user = userService.createUser(smith, password);

        assertEquals(smith, user.getUsername());
        assertEquals(hashedPassword, user.getHashedPassword().getHashedValue());
        assertEquals(salt, user.getHashedPassword().getSalt());
        assertTrue(user.getRoles().isEmpty());
    }

    @Test
    void delete_user_fails_if_user_does_not_exist() {
        Exception e = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUser(new User(smith, null)));
        assertEquals("User smith does not exist", e.getMessage());
    }

    @Test
    void delete_existing_user() {
        User user = userService.createUser(smith, password);

        userService.deleteUser(user);

        assertFalse(userService.exists(smith));
    }

    @Test
    void get_user_fails_if_user_does_not_exist() {
        Exception e = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUser(smith));
        assertEquals("User smith does not exist", e.getMessage());
    }

    @Test
    void get_existing_user() {
        userService.createUser(smith, password);

        User user = userService.getUser(smith);

        assertEquals(smith, user.getUsername());
    }

    @Test
    void authenticate_user_fails_if_user_does_not_exist() {
        Exception e = assertThrows(
                IllegalArgumentException.class,
                () -> userService.authenticateUser(smith, password));
        assertEquals("User smith does not exist", e.getMessage());
    }

    @Test
    void authenticate_user_fails_if_password_is_invalid() {
        String wrongPassword = "wrongPassword";
        byte[] hashedWrongPassword = "hashedWrongPassword".getBytes(UTF_8);

        when(passwordHasher.hash(password))
                .thenReturn(new HashedPassword(hashedPassword, salt));
        when(passwordHasher.hash(wrongPassword, salt))
                .thenReturn(new HashedPassword(hashedWrongPassword, salt));

        userService.createUser(smith, password);

        Exception e = assertThrows(
                IllegalArgumentException.class,
                () -> userService.authenticateUser(smith, wrongPassword));
        assertEquals("Invalid password for smith", e.getMessage());
    }

    @Test
    void authenticate_user_with_correct_password() {
        when(passwordHasher.hash(password))
                .thenReturn(new HashedPassword(hashedPassword, salt));
        when(passwordHasher.hash(password, salt))
                .thenReturn(new HashedPassword(hashedPassword, salt));

        userService.createUser(smith, password);

        userService.authenticateUser(smith, password);
    }

}