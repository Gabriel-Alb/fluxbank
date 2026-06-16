package com.gabriel.fluxbank.modules.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<ValidCpf, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.isBlank()) {
            return true;
        }

        String normalizedCpf = cpf.replaceAll("\\D", "");

        if (!normalizedCpf.matches("\\d{11}")) {
            return false;
        }

        if (normalizedCpf.chars().distinct().count() == 1) {
            return false;
        }

        int firstCheckDigit = calculateCheckDigit(normalizedCpf, 10);

        if (firstCheckDigit != Character.getNumericValue(normalizedCpf.charAt(9))) {
            return false;
        }

        int secondCheckDigit = calculateCheckDigit(normalizedCpf, 11);

        return secondCheckDigit
                == Character.getNumericValue(normalizedCpf.charAt(10));
    }

    private int calculateCheckDigit(String cpf, int initialWeight) {
        int sum = 0;
        int numberOfDigits = initialWeight - 1;

        for (int index = 0; index < numberOfDigits; index++) {
            int digit = Character.getNumericValue(cpf.charAt(index));
            int weight = initialWeight - index;

            sum += digit * weight;
        }

        int remainder = sum % 11;

        return remainder < 2 ? 0 : 11 - remainder;
    }
}