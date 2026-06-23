package com.gabriel.fluxbank.modules.auth.dto.response;

import java.util.UUID;

import com.gabriel.fluxbank.modules.user.enums.UserStatus;

public record AuthenticatedUserResponse(
        UUID id,
        String fullName,
        String email,
        UserStatus status,
        boolean emailVerified
) {
}