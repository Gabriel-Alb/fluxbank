package com.gabriel.fluxbank.modules.user.entity;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;

import com.gabriel.fluxbank.modules.user.enums.UserStatus;
import com.gabriel.fluxbank.shared.audit.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_users_email_lookup_hash",
                        columnNames = "email_lookup_hash"
                ),
                @UniqueConstraint(
                        name = "uk_users_cpf_lookup_hash",
                        columnNames = "cpf_lookup_hash"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AuditableEntity {

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

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "email_encrypted", nullable = false)
    private byte[] emailEncrypted;

    @Column(name = "email_nonce", nullable = false)
    private byte[] emailNonce;

    @Column(name = "email_lookup_hash", nullable = false)
    private byte[] emailLookupHash;

    @Column(name = "email_key_version", nullable = false)
    private short emailKeyVersion;

    @Column(name = "cpf_encrypted", nullable = false)
    private byte[] cpfEncrypted;

    @Column(name = "cpf_nonce", nullable = false)
    private byte[] cpfNonce;

    @Column(name = "cpf_lookup_hash", nullable = false)
    private byte[] cpfLookupHash;

    @Column(name = "cpf_key_version", nullable = false)
    private short cpfKeyVersion;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private UserStatus status;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts;

    @Column(name = "locked_until")
    private OffsetDateTime lockedUntil;

    @Column(name = "password_changed_at", nullable = false)
    private OffsetDateTime passwordChangedAt;

    public User(
            String fullName,
            byte[] emailEncrypted,
            byte[] emailNonce,
            byte[] emailLookupHash,
            short emailKeyVersion,
            byte[] cpfEncrypted,
            byte[] cpfNonce,
            byte[] cpfLookupHash,
            short cpfKeyVersion,
            String passwordHash
    ) {
        this.fullName = fullName;
        this.emailEncrypted = emailEncrypted.clone();
        this.emailNonce = emailNonce.clone();
        this.emailLookupHash = emailLookupHash.clone();
        this.emailKeyVersion = emailKeyVersion;
        this.cpfEncrypted = cpfEncrypted.clone();
        this.cpfNonce = cpfNonce.clone();
        this.cpfLookupHash = cpfLookupHash.clone();
        this.cpfKeyVersion = cpfKeyVersion;
        this.passwordHash = passwordHash;
        this.status = UserStatus.PENDING_VERIFICATION;
        this.emailVerified = false;
        this.failedLoginAttempts = 0;
        this.passwordChangedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public void markEmailAsVerified() {
        this.emailVerified = true;

        if (this.status == UserStatus.PENDING_VERIFICATION) {
            this.status = UserStatus.ACTIVE;
        }
    }
}