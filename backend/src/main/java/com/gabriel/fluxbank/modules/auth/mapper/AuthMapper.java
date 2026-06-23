package com.gabriel.fluxbank.modules.auth.mapper;

import org.springframework.stereotype.Component;

import com.gabriel.fluxbank.modules.auth.dto.response.AuthenticatedUserResponse;
import com.gabriel.fluxbank.modules.user.entity.User;
import com.gabriel.fluxbank.shared.security.DataProtectionService;

@Component
public class AuthMapper {

    private final DataProtectionService dataProtectionService;

    public AuthMapper(DataProtectionService dataProtectionService) {
        this.dataProtectionService = dataProtectionService;
    }

    public AuthenticatedUserResponse toAuthenticatedUserResponse(User user) {
        String email = dataProtectionService.decrypt(
                user.getEmailEncrypted(),
                user.getEmailNonce(),
                user.getEmailKeyVersion()
        );

        return new AuthenticatedUserResponse(
                user.getId(),
                user.getFullName(),
                email,
                user.getStatus(),
                user.isEmailVerified()
        );
    }
}