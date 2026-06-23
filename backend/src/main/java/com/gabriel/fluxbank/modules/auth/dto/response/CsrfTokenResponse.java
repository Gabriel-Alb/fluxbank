package com.gabriel.fluxbank.modules.auth.dto.response;

public record CsrfTokenResponse(
        String headerName,
        String parameterName,
        String token
) {
}