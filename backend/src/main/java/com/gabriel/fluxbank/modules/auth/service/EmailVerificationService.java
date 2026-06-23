package com.gabriel.fluxbank.modules.auth.service;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gabriel.fluxbank.exception.BusinessException;
import com.gabriel.fluxbank.modules.auth.dto.response.EmailVerificationResponse;
import com.gabriel.fluxbank.modules.auth.entity.EmailVerificationToken;
import com.gabriel.fluxbank.modules.auth.repository.EmailVerificationTokenRepository;
import com.gabriel.fluxbank.modules.user.entity.User;
import com.gabriel.fluxbank.modules.user.enums.UserStatus;
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

    @Transactional
    public EmailVerificationResponse verifyEmail(String rawToken) {
        String normalizedToken = normalizeToken(rawToken);

        byte[] tokenHash = dataProtectionService.createLookupHash(
                normalizedToken
        );

        EmailVerificationToken verificationToken = tokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(() -> new BusinessException(
                        "Invalid email verification token",
                        HttpStatus.BAD_REQUEST
                ));

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        if (verificationToken.isUsed()) {
            throw new BusinessException(
                    "Email verification token has already been used",
                    HttpStatus.CONFLICT
            );
        }

        if (verificationToken.isExpired(now)) {
            throw new BusinessException(
                    "Email verification token has expired",
                    HttpStatus.BAD_REQUEST
            );
        }

        User user = verificationToken.getUser();

        validateUserCanBeVerified(user);

        verificationToken.markAsUsed();
        user.markEmailAsVerified();

        return new EmailVerificationResponse(
                true,
                "Email verified successfully"
        );
    }

    private void validateUserCanBeVerified(User user) {
        if (user.getStatus() == UserStatus.SUSPENDED) {
            throw new BusinessException(
                    "User account is suspended",
                    HttpStatus.FORBIDDEN
            );
        }

        if (user.getStatus() == UserStatus.DEACTIVATED) {
            throw new BusinessException(
                    "User account is deactivated",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    private String normalizeToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            throw new BusinessException(
                    "Verification token is required",
                    HttpStatus.BAD_REQUEST
            );
        }

        return rawToken.trim();
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