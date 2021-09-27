package com.winc.auth.service.util;

import java.util.Random;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TokenGenerator {

    public String generate() {
        byte[] array = new byte[32];
        new Random().nextBytes(array);
        return new String(array, UTF_8);
    }
}
