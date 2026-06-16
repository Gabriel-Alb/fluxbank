package com.gabriel.fluxbank.modules.user.mapper;

import org.springframework.stereotype.Component;

import com.gabriel.fluxbank.modules.user.dto.response.UserResponse;
import com.gabriel.fluxbank.modules.user.entity.User;
import com.gabriel.fluxbank.shared.security.DataProtectionService;

@Component
public class UserMapper {

    private final DataProtectionService dataProtectionService;

    public UserMapper(DataProtectionService dataProtectionService) {
        this.dataProtectionService = dataProtectionService;
    }

    public UserResponse toResponse(User user) {
        String email = dataProtectionService.decrypt(
                user.getEmailEncrypted(),
                user.getEmailNonce(),
                user.getEmailKeyVersion()
        );

        String cpf = dataProtectionService.decrypt(
                user.getCpfEncrypted(),
                user.getCpfNonce(),
                user.getCpfKeyVersion()
        );

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                email,
                maskCpf(cpf),
                user.getStatus(),
                user.isEmailVerified(),
                user.getCreatedAt()
        );
    }

    private String maskCpf(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new IllegalStateException(
                    "Stored CPF has an invalid format"
            );
        }

        return "***.***.***-" + cpf.substring(9);
    }
}