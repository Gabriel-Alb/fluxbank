package com.gabriel.fluxbank.modules.auth.dto.response;

public record EmailVerificationResponse(
        boolean verified,
        String message
) {
}