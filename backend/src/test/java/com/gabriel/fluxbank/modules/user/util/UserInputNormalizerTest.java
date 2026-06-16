package com.gabriel.fluxbank.modules.user.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class UserInputNormalizerTest {

    @Test
    void shouldTrimAndReplaceMultipleSpacesInFullName() {
        String normalizedName = UserInputNormalizer.normalizeFullName(
                "  Gabriel   Albuquerque   Silva  "
        );

        assertEquals(
                "Gabriel Albuquerque Silva",
                normalizedName
        );
    }

    @Test
    void shouldTrimAndConvertEmailToLowercase() {
        String normalizedEmail = UserInputNormalizer.normalizeEmail(
                "  Gabriel.Albuquerque@Email.COM  "
        );

        assertEquals(
                "gabriel.albuquerque@email.com",
                normalizedEmail
        );
    }

    @Test
    void shouldRemoveCpfFormatting() {
        String normalizedCpf = UserInputNormalizer.normalizeCpf(
                "529.982.247-25"
        );

        assertEquals(
                "52998224725",
                normalizedCpf
        );
    }

    @Test
    void shouldKeepAlreadyNormalizedCpf() {
        String normalizedCpf = UserInputNormalizer.normalizeCpf(
                "52998224725"
        );

        assertEquals(
                "52998224725",
                normalizedCpf
        );
    }
}