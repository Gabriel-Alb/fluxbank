package com.gabriel.fluxbank.shared.security;

import java.util.Objects;

public record ProtectedData(
        byte[] encryptedValue,
        byte[] nonce,
        byte[] lookupHash,
        short keyVersion
) {

    public ProtectedData {
        Objects.requireNonNull(encryptedValue);
        Objects.requireNonNull(nonce);
        Objects.requireNonNull(lookupHash);

        encryptedValue = encryptedValue.clone();
        nonce = nonce.clone();
        lookupHash = lookupHash.clone();
    }

    @Override
    public byte[] encryptedValue() {
        return encryptedValue.clone();
    }

    @Override
    public byte[] nonce() {
        return nonce.clone();
    }

    @Override
    public byte[] lookupHash() {
        return lookupHash.clone();
    }
}