package com.gabriel.fluxbank.modules.user.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PasswordValidatorTest {

    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
    }

    @Test
    void shouldAcceptValidPassword() {
        boolean valid = passwordValidator.isValid(
                "FluxBank1!",
                null
        );

        assertTrue(valid);
    }

    @Test
    void shouldRejectPasswordWithoutUppercaseLetter() {
        boolean valid = passwordValidator.isValid(
                "fluxbank1!",
                null
        );

        assertFalse(valid);
    }

    @Test
    void shouldRejectPasswordWithoutNumber() {
        boolean valid = passwordValidator.isValid(
                "FluxBank!",
                null
        );

        assertFalse(valid);
    }

    @Test
    void shouldRejectPasswordWithoutSpecialCharacter() {
        boolean valid = passwordValidator.isValid(
                "FluxBank1",
                null
        );

        assertFalse(valid);
    }

    @Test
    void shouldRejectPasswordContainingWhitespace() {
        boolean valid = passwordValidator.isValid(
                "Flux Bank1!",
                null
        );

        assertFalse(valid);
    }

    @Test
    void shouldRejectPasswordShorterThanMinimumLength() {
        boolean valid = passwordValidator.isValid(
                "Flux1!",
                null
        );

        assertFalse(valid);
    }

    @Test
    void shouldRejectPasswordLongerThanMaximumLength() {
        String password = "A1!" + "a".repeat(62);

        boolean valid = passwordValidator.isValid(
                password,
                null
        );

        assertFalse(valid);
    }

    @Test
    void shouldIgnoreNullBecauseNotBlankHandlesRequiredValue() {
        boolean valid = passwordValidator.isValid(
                null,
                null
        );

        assertTrue(valid);
    }

    @Test
    void shouldIgnoreBlankValueBecauseNotBlankHandlesRequiredValue() {
        boolean valid = passwordValidator.isValid(
                " ",
                null
        );

        assertTrue(valid);
    }
}