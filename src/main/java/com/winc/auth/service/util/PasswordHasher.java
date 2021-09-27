package com.winc.auth.service.util;

import com.winc.auth.entity.HashedPassword;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PasswordHasher {

    // reference: https://www.baeldung.com/java-password-hashing

    public HashedPassword hash(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return hash(password, salt);
    }

    public HashedPassword hash(String password, byte[] salt) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        digest.update(salt);

        byte[] hashed = digest.digest(password.getBytes(UTF_8));
        return new HashedPassword(hashed, salt);
    }
}
