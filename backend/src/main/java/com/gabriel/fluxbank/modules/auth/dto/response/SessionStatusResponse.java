package com.gabriel.fluxbank.modules.auth.dto.response;

public record SessionStatusResponse(
        boolean authenticated,
        AuthenticatedUserResponse user
) {
}