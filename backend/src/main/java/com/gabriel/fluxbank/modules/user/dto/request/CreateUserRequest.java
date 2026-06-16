package com.gabriel.fluxbank.modules.user.dto.request;

import com.gabriel.fluxbank.modules.user.validation.ValidCpf;
import com.gabriel.fluxbank.modules.user.validation.ValidPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

        @NotBlank(message = "Full name is required")
        @Size(
                min = 3,
                max = 150,
                message = "Full name must be between 3 and 150 characters"
        )
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(
                max = 254,
                message = "Email must not exceed 254 characters"
        )
        String email,

        @NotBlank(message = "CPF is required")
        @Pattern(
                regexp = "\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
                message = "CPF must contain 11 digits or use the format 000.000.000-00"
        )
        @ValidCpf
        String cpf,

        @NotBlank(message = "Password is required")
        @Size(
                min = 8,
                max = 64,
                message = "Password must be between 8 and 64 characters"
        )
        @ValidPassword
        String password,

        @NotBlank(message = "Password confirmation is required")
        @Size(
                min = 8,
                max = 64,
                message = "Password confirmation must be between 8 and 64 characters"
        )
        String passwordConfirmation

) {
}