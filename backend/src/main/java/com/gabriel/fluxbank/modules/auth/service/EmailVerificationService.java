package com.gabriel.fluxbank.modules.auth.service;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gabriel.fluxbank.modules.auth.entity.EmailVerificationToken;
import com.gabriel.fluxbank.modules.auth.repository.EmailVerificationTokenRepository;
import com.gabriel.fluxbank.modules.user.entity.User;
import com.gabriel.fluxbank.shared.security.DataProtectionService;

@Service
public class EmailVerificationService {

    private static final int TOKEN_RANDOM_BYTES = 32;
    private static final int TOKEN_EXPIRATION_HOURS = 24;
    private static final int MAX_GENERATION_ATTEMPTS = 5;

    private final EmailVerificationTokenRepository tokenRepository;
    private final DataProtectionService dataProtectionService;
    private final SecureRandom secureRandom;

    public EmailVerificationService(
            EmailVerificationTokenRepository tokenRepository,
            DataProtectionService dataProtectionService
    ) {
        this.tokenRepository = tokenRepository;
        this.dataProtectionService = dataProtectionService;
        this.secureRandom = new SecureRandom();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public String generateVerificationToken(User user) {
        for (int attempt = 0; attempt < MAX_GENERATION_ATTEMPTS; attempt++) {
            String rawToken = generateRawToken();

            byte[] tokenHash = dataProtectionService.createLookupHash(
                    rawToken
            );

            if (tokenRepository.existsByTokenHash(tokenHash)) {
                continue;
            }

            OffsetDateTime expiresAt = OffsetDateTime
                    .now(ZoneOffset.UTC)
                    .plusHours(TOKEN_EXPIRATION_HOURS);

            EmailVerificationToken verificationToken =
                    new EmailVerificationToken(
                            user,
                            tokenHash,
                            expiresAt
                    );

            tokenRepository.save(verificationToken);

            return rawToken;
        }

        throw new IllegalStateException(
                "Unable to generate a unique email verification token"
        );
    }

    private String generateRawToken() {
        byte[] randomBytes = new byte[TOKEN_RANDOM_BYTES];

        secureRandom.nextBytes(randomBytes);

        return Base64
                .getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }
}