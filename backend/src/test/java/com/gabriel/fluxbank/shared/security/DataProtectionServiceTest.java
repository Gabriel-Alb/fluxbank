package com.gabriel.fluxbank.shared.security;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataProtectionServiceTest {

    private static final String ENCRYPTION_KEY = Base64.getEncoder()
            .encodeToString(
                    "0123456789ABCDEF0123456789ABCDEF"
                            .getBytes(StandardCharsets.UTF_8)
            );

    private static final String HMAC_KEY = Base64.getEncoder()
            .encodeToString(
                    "FEDCBA9876543210FEDCBA9876543210"
                            .getBytes(StandardCharsets.UTF_8)
            );

    private DataProtectionService dataProtectionService;

    @BeforeEach
    void setUp() {
        dataProtectionService = new DataProtectionService(
                ENCRYPTION_KEY,
                HMAC_KEY,
                (short) 1
        );
    }

    @Test
    void shouldEncryptAndDecryptValue() {
        ProtectedData protectedData = dataProtectionService.protect(
                "gabriel@email.com"
        );

        String decryptedValue = dataProtectionService.decrypt(
                protectedData.encryptedValue(),
                protectedData.nonce(),
                protectedData.keyVersion()
        );

        assertEquals(
                "gabriel@email.com",
                decryptedValue
        );
    }

    @Test
    void shouldGenerateDifferentCiphertextsForSameValue() {
        ProtectedData first = dataProtectionService.protect(
                "gabriel@email.com"
        );

        ProtectedData second = dataProtectionService.protect(
                "gabriel@email.com"
        );

        assertFalse(
                Arrays.equals(
                        first.encryptedValue(),
                        second.encryptedValue()
                )
        );

        assertFalse(
                Arrays.equals(
                        first.nonce(),
                        second.nonce()
                )
        );

        assertArrayEquals(
                first.lookupHash(),
                second.lookupHash()
        );
    }

    @Test
    void shouldGenerateDifferentHashesForDifferentValues() {
        byte[] firstHash = dataProtectionService.createLookupHash(
                "gabriel@email.com"
        );

        byte[] secondHash = dataProtectionService.createLookupHash(
                "outro@email.com"
        );

        assertFalse(Arrays.equals(firstHash, secondHash));
    }

    @Test
    void shouldDetectModifiedEncryptedValue() {
        ProtectedData protectedData = dataProtectionService.protect(
                "52998224725"
        );

        byte[] modifiedValue = protectedData.encryptedValue();
        modifiedValue[0] = (byte) (modifiedValue[0] ^ 1);

        assertThrows(
                IllegalStateException.class,
                () -> dataProtectionService.decrypt(
                        modifiedValue,
                        protectedData.nonce(),
                        protectedData.keyVersion()
                )
        );
    }

    @Test
    void shouldRejectInvalidEncryptionKeyLength() {
        String invalidKey = Base64.getEncoder()
                .encodeToString(
                        "short-key".getBytes(StandardCharsets.UTF_8)
                );

        assertThrows(
                IllegalStateException.class,
                () -> new DataProtectionService(
                        invalidKey,
                        HMAC_KEY,
                        (short) 1
                )
        );
    }

    @Test
    void shouldRejectUnsupportedKeyVersion() {
        ProtectedData protectedData = dataProtectionService.protect(
                "52998224725"
        );

        assertThrows(
                IllegalStateException.class,
                () -> dataProtectionService.decrypt(
                        protectedData.encryptedValue(),
                        protectedData.nonce(),
                        (short) 2
                )
        );
    }

    @Test
    void shouldStoreCurrentKeyVersion() {
        ProtectedData protectedData = dataProtectionService.protect(
                "52998224725"
        );

        assertEquals(
                1,
                protectedData.keyVersion()
        );
    }

    @Test
    void shouldNotExposeInternalByteArrays() {
        ProtectedData protectedData = dataProtectionService.protect(
                "52998224725"
        );

        byte[] firstRead = protectedData.lookupHash();
        byte originalValue = firstRead[0];

        firstRead[0] = (byte) (firstRead[0] ^ 1);

        byte[] secondRead = protectedData.lookupHash();

        assertNotEquals(
                firstRead[0],
                secondRead[0]
        );

        assertEquals(
                originalValue,
                secondRead[0]
        );
    }
}