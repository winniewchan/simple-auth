package com.winc.auth.service.util;

import com.winc.auth.entity.AuthToken;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.*;

class AuthClockTest {

    private static final String now = "2021-09-01T10:15:30";
    private static final String aboutOneHourLater = "2021-09-01T11:08:10";
    private static final String exactlyTwoHoursLater = "2021-09-01T12:15:30";
    private static final String moreThanTwoHoursLater = "2021-09-01T12:31:40";

    @Test
    void calculate_2_hours_from_now() {
        assertEquals(
                LocalDateTime.parse(exactlyTwoHoursLater),
                currentTime(now).twoHoursFromNow());
    }

    @Test
    void check_if_token_is_expired_against_current_time() {
        AuthToken authToken = new AuthToken(
                "user",
                LocalDateTime.parse(exactlyTwoHoursLater));

        assertFalse(currentTime(now)
                .isExpired(authToken));
        assertFalse(currentTime(aboutOneHourLater)
                .isExpired(authToken));
        assertFalse(currentTime(exactlyTwoHoursLater)
                .isExpired(authToken));
        assertTrue(currentTime(moreThanTwoHoursLater)
                .isExpired(authToken));
    }

    private AuthClock currentTime(String now) {
        return new AuthClock(
                Clock.fixed(
                        Instant.parse(now + ".00Z"),
                        UTC));
    }

}