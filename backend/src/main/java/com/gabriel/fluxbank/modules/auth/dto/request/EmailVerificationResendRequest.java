package com.gabriel.fluxbank.modules.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmailVerificationResendRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(
                max = 254,
                message = "Email must not exceed 254 characters"
        )
        String email
) {
}