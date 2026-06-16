package com.gabriel.fluxbank.shared.security;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DataProtectionService {

    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private static final int AES_KEY_LENGTH_BYTES = 32;
    private static final int HMAC_MINIMUM_KEY_LENGTH_BYTES = 32;
    private static final int NONCE_LENGTH_BYTES = 12;
    private static final int AUTHENTICATION_TAG_LENGTH_BITS = 128;

    private final SecretKey encryptionKey;
    private final SecretKey hmacKey;
    private final short keyVersion;
    private final SecureRandom secureRandom;

    public DataProtectionService(
            @Value("${app.crypto.encryption-key}")
            String encryptionKeyBase64,

            @Value("${app.crypto.hmac-key}")
            String hmacKeyBase64,

            @Value("${app.crypto.key-version}")
            short keyVersion
    ) {
        byte[] encryptionKeyBytes = decodeBase64Key(
                encryptionKeyBase64,
                "Encryption key"
        );

        byte[] hmacKeyBytes = decodeBase64Key(
                hmacKeyBase64,
                "HMAC key"
        );

        validateEncryptionKey(encryptionKeyBytes);
        validateHmacKey(hmacKeyBytes);

        if (keyVersion <= 0) {
            throw new IllegalStateException(
                    "Crypto key version must be greater than zero"
            );
        }

        this.encryptionKey = new SecretKeySpec(
                encryptionKeyBytes,
                AES_ALGORITHM
        );

        this.hmacKey = new SecretKeySpec(
                hmacKeyBytes,
                HMAC_ALGORITHM
        );

        this.keyVersion = keyVersion;
        this.secureRandom = new SecureRandom();
    }

    public ProtectedData protect(String normalizedValue) {
        validateValue(normalizedValue);

        byte[] nonce = new byte[NONCE_LENGTH_BYTES];
        secureRandom.nextBytes(nonce);

        byte[] encryptedValue = encrypt(normalizedValue, nonce);
        byte[] lookupHash = createLookupHash(normalizedValue);

        return new ProtectedData(
                encryptedValue,
                nonce,
                lookupHash,
                keyVersion
        );
    }

    public byte[] createLookupHash(String normalizedValue) {
        validateValue(normalizedValue);

        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(hmacKey);

            return mac.doFinal(
                    normalizedValue.getBytes(StandardCharsets.UTF_8)
            );
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException(
                    "Unable to generate sensitive data lookup hash",
                    exception
            );
        }
    }

    public String decrypt(
            byte[] encryptedValue,
            byte[] nonce,
            short encryptedWithKeyVersion
    ) {
        Objects.requireNonNull(encryptedValue);
        Objects.requireNonNull(nonce);

        if (encryptedWithKeyVersion != keyVersion) {
            throw new IllegalStateException(
                    "Unsupported crypto key version"
            );
        }

        if (nonce.length != NONCE_LENGTH_BYTES) {
            throw new IllegalArgumentException(
                    "Nonce must contain exactly 12 bytes"
            );
        }

        try {
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);

            GCMParameterSpec parameters = new GCMParameterSpec(
                    AUTHENTICATION_TAG_LENGTH_BITS,
                    nonce
            );

            cipher.init(
                    Cipher.DECRYPT_MODE,
                    encryptionKey,
                    parameters
            );

            byte[] decryptedValue = cipher.doFinal(encryptedValue);

            return new String(
                    decryptedValue,
                    StandardCharsets.UTF_8
            );
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException(
                    "Unable to decrypt sensitive data",
                    exception
            );
        }
    }

    private byte[] encrypt(String value, byte[] nonce) {
        try {
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);

            GCMParameterSpec parameters = new GCMParameterSpec(
                    AUTHENTICATION_TAG_LENGTH_BITS,
                    nonce
            );

            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    encryptionKey,
                    parameters
            );

            return cipher.doFinal(
                    value.getBytes(StandardCharsets.UTF_8)
            );
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException(
                    "Unable to encrypt sensitive data",
                    exception
            );
        }
    }

    private static byte[] decodeBase64Key(
            String encodedKey,
            String keyName
    ) {
        if (encodedKey == null || encodedKey.isBlank()) {
            throw new IllegalStateException(
                    keyName + " must be configured"
            );
        }

        try {
            return Base64.getDecoder().decode(encodedKey);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(
                    keyName + " must use valid Base64",
                    exception
            );
        }
    }

    private static void validateEncryptionKey(byte[] key) {
        if (key.length != AES_KEY_LENGTH_BYTES) {
            throw new IllegalStateException(
                    "Encryption key must contain exactly 32 bytes"
            );
        }
    }

    private static void validateHmacKey(byte[] key) {
        if (key.length < HMAC_MINIMUM_KEY_LENGTH_BYTES) {
            throw new IllegalStateException(
                    "HMAC key must contain at least 32 bytes"
            );
        }
    }

    private static void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Value to protect must not be blank"
            );
        }
    }
}