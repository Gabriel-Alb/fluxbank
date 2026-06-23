package com.gabriel.fluxbank.modules.auth.entity;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;

import com.gabriel.fluxbank.modules.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "email_verification_tokens",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_email_verification_tokens_token_hash",
                        columnNames = "token_hash"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerificationToken {

    @Id
    @Generated
    @ColumnDefault("uuidv7()")
    @Column(
            name = "id",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Column(name = "token_hash", nullable = false)
    private byte[] tokenHash;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    @Column(name = "used_at")
    private OffsetDateTime usedAt;

    @Column(name = "revoked_at")
    private OffsetDateTime revokedAt;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private OffsetDateTime createdAt;

    public EmailVerificationToken(
            User user,
            byte[] tokenHash,
            OffsetDateTime expiresAt
    ) {
        this.user = user;
        this.tokenHash = tokenHash.clone();
        this.expiresAt = expiresAt;
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public boolean isUsed() {
        return usedAt != null;
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public boolean isExpired(OffsetDateTime now) {
        return !expiresAt.isAfter(now);
    }

    public void markAsUsed() {
        this.usedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public void markAsRevoked() {
        this.revokedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public byte[] getTokenHash() {
        return tokenHash.clone();
    }
}