package com.gabriel.fluxbank.modules.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator
        implements ConstraintValidator<ValidPassword, String> {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 64;

    @Override
    public boolean isValid(
            String password,
            ConstraintValidatorContext context
    ) {
        if (password == null || password.isBlank()) {
            return true;
        }

        if (password.length() < MIN_LENGTH
                || password.length() > MAX_LENGTH) {
            return false;
        }

        boolean hasUppercaseLetter = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        for (char character : password.toCharArray()) {
            if (Character.isWhitespace(character)) {
                return false;
            }

            if (Character.isUpperCase(character)) {
                hasUppercaseLetter = true;
            } else if (Character.isDigit(character)) {
                hasNumber = true;
            } else if (!Character.isLetter(character)) {
                hasSpecialCharacter = true;
            }
        }

        return hasUppercaseLetter
                && hasNumber
                && hasSpecialCharacter;
    }
}