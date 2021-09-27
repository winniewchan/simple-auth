package com.winc.auth.service.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenGeneratorTest {

    @Test
    void generate_random_token_of_maximum_32_characters() {
        String token = new TokenGenerator().generate();

        assertTrue(token.length() <= 32);
    }
}