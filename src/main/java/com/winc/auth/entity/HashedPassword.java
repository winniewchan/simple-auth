package com.winc.auth.entity;

import java.util.Arrays;

public class HashedPassword {

    private final byte[] hashedValue;

    private final byte[] salt;

    public HashedPassword(byte[] hashedPassword, byte[] salt) {
        this.hashedValue = hashedPassword;
        this.salt = salt;
    }

    public byte[] getHashedValue() {
        return hashedValue;
    }

    public byte[] getSalt() {
        return salt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashedPassword that = (HashedPassword) o;
        return Arrays.equals(hashedValue, that.hashedValue) && Arrays.equals(salt, that.salt);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(hashedValue);
        result = 31 * result + Arrays.hashCode(salt);
        return result;
    }
}
