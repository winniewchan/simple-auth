package com.winc.auth.service.util;

import com.winc.auth.entity.HashedPassword;
import org.junit.jupiter.api.Test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {

    PasswordHasher passwordHasher = new PasswordHasher();

    @Test
    void hash_plain_text_password() {
        String password = "SomeSecret";

        HashedPassword hashedPassword = passwordHasher.hash(password);

        assertNotEquals(password, new String(hashedPassword.getHashedValue()));
        assertNotNull(hashedPassword.getSalt());
    }

    @Test
    void same_value_hashed_with_same_salt_gives_same_password() {
        String password = "SomeSecret";
        String otherPassword = "OtherSecrets";
        byte[] salt = "SaltAndPepper".getBytes(UTF_8);

        HashedPassword hashedPassword = passwordHasher.hash(password, salt);
        HashedPassword sameHashedPassword = passwordHasher.hash(password, salt);
        HashedPassword otherHashedPassword = passwordHasher.hash(otherPassword, salt);

        assertEquals(hashedPassword, sameHashedPassword);
        assertNotEquals(hashedPassword, otherHashedPassword);
    }
}