package com.gabriel.fluxbank.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class PasswordEncoderConfigTest {

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new PasswordEncoderConfig().passwordEncoder();
    }

    @Test
    void shouldEncodePasswordUsingArgon2id() {
        String rawPassword = "FluxBank1!";

        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertTrue(encodedPassword.startsWith("$argon2id$"));
        assertNotEquals(rawPassword, encodedPassword);
    }

    @Test
    void shouldMatchCorrectPassword() {
        String rawPassword = "FluxBank1!";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void shouldRejectIncorrectPassword() {
        String encodedPassword = passwordEncoder.encode("FluxBank1!");

        assertFalse(
                passwordEncoder.matches(
                        "Incorrect1!",
                        encodedPassword
                )
        );
    }

    @Test
    void shouldGenerateDifferentHashesForSamePassword() {
        String rawPassword = "FluxBank1!";

        String firstHash = passwordEncoder.encode(rawPassword);
        String secondHash = passwordEncoder.encode(rawPassword);

        assertNotEquals(firstHash, secondHash);
        assertTrue(passwordEncoder.matches(rawPassword, firstHash));
        assertTrue(passwordEncoder.matches(rawPassword, secondHash));
    }
}