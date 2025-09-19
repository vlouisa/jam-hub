package dev.louisa.jam.hub.infrastructure.security.jwt.common;

import com.nimbusds.jose.jwk.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class JwtKeys {

    private final List<JwtKey> allKeys;       // All loaded keys (active + inactive)
    private final JwtKey activeSigningKey;    // The key used for signing new tokens

    public JwtKeys(List<JwtKey> keys) {
        this.allKeys = keys;
        this.activeSigningKey = keys.stream()
                .filter(this::isActive) // define your active check
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No active JWT key"));
    }

    public JwtKey activeKey() {
        return activeSigningKey;
    }

    public JwtKey keyForId(UUID kid) {
        return allKeys.stream()
                .filter(k -> k.kid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Key not found for kid: " + kid));
    }

    public JWKSet toJWKSet() {
        return new JWKSet(allKeys.stream().map(JwtKey::rsaKey).collect(Collectors.toUnmodifiableList()));
    }

    private boolean isActive(JwtKey key) {
        return "ACTIVE".equalsIgnoreCase(key.status());
    }
}