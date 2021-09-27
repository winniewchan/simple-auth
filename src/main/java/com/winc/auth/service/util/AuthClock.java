package com.winc.auth.service.util;

import com.winc.auth.entity.AuthToken;

import java.time.Clock;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.HOURS;

public class AuthClock {

    private final Clock clock;

    public AuthClock(Clock clock) {
        this.clock = clock;
    }

    public LocalDateTime twoHoursFromNow() {
        return LocalDateTime.now(clock)
                .plus(2, HOURS);
    }

    public boolean isExpired(AuthToken authToken) {
        return LocalDateTime.now(clock)
                .isAfter(authToken.getExpiry());
    }
}
