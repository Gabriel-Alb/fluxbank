package com.gabriel.fluxbank.modules.auth.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriel.fluxbank.modules.auth.entity.EmailVerificationToken;
import com.gabriel.fluxbank.modules.user.entity.User;

public interface EmailVerificationTokenRepository
        extends JpaRepository<EmailVerificationToken, UUID> {

    Optional<EmailVerificationToken> findByTokenHash(byte[] tokenHash);

    boolean existsByTokenHash(byte[] tokenHash);

    List<EmailVerificationToken> findAllByUserAndUsedAtIsNullAndRevokedAtIsNull(
            User user
    );
}