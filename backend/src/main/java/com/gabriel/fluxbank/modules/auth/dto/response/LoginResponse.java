package com.gabriel.fluxbank.modules.auth.dto.response;

public record LoginResponse(
        AuthenticatedUserResponse user
) {
}