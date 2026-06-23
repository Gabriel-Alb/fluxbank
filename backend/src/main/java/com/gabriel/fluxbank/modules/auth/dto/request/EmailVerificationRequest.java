package com.gabriel.fluxbank.modules.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmailVerificationRequest(

        @NotBlank(message = "Verification token is required")
        @Size(
                min = 32,
                max = 512,
                message = "Verification token has an invalid size"
        )
        String token
) {
}