package com.gabriel.fluxbank.modules.user.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.gabriel.fluxbank.modules.user.enums.UserStatus;

public record UserResponse(
        UUID id,
        String fullName,
        String email,
        String cpfMasked,
        UserStatus status,
        boolean emailVerified,
        OffsetDateTime createdAt
) {
}