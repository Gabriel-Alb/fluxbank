package com.gabriel.fluxbank.modules.user.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CpfValidatorTest {

    private CpfValidator cpfValidator;

    @BeforeEach
    void setUp() {
        cpfValidator = new CpfValidator();
    }

    @Test
    void shouldAcceptValidCpfWithoutFormatting() {
        boolean valid = cpfValidator.isValid(
                "52998224725",
                null
        );

        assertTrue(valid);
    }

    @Test
    void shouldAcceptValidFormattedCpf() {
        boolean valid = cpfValidator.isValid(
                "529.982.247-25",
                null
        );

        assertTrue(valid);
    }

    @Test
    void shouldRejectCpfWithInvalidCheckDigits() {
        boolean valid = cpfValidator.isValid(
                "52998224724",
                null
        );

        assertFalse(valid);
    }

    @Test
    void shouldRejectCpfWithRepeatedDigits() {
        boolean valid = cpfValidator.isValid(
                "11111111111",
                null
        );

        assertFalse(valid);
    }

    @Test
    void shouldRejectCpfWithInvalidLength() {
        boolean valid = cpfValidator.isValid(
                "5299822472",
                null
        );

        assertFalse(valid);
    }

    @Test
    void shouldIgnoreNullBecauseNotBlankHandlesRequiredValue() {
        boolean valid = cpfValidator.isValid(
                null,
                null
        );

        assertTrue(valid);
    }

    @Test
    void shouldIgnoreBlankValueBecauseNotBlankHandlesRequiredValue() {
        boolean valid = cpfValidator.isValid(
                " ",
                null
        );

        assertTrue(valid);
    }
}