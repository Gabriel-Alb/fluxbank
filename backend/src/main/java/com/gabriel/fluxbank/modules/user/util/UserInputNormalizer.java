package com.gabriel.fluxbank.modules.user.util;

import java.util.Locale;

public final class UserInputNormalizer {

    private UserInputNormalizer() {
    }

    public static String normalizeFullName(String fullName) {
        return fullName
                .trim()
                .replaceAll("\\s+", " ");
    }

    public static String normalizeEmail(String email) {
        return email
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    public static String normalizeCpf(String cpf) {
        return cpf.replaceAll("\\D", "");
    }
}