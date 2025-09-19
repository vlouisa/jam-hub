package dev.louisa.jam.hub.infrastructure.security.jwt.common;

import com.nimbusds.jose.jwk.*;
import dev.louisa.jam.hub.infrastructure.security.exception.SecurityException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.JWT_KEY_RESOLVER_ERROR;

@Slf4j
public class JwtKeys {

    private final List<JwtKey> allKeys;
    private final JwtKey activeSigningKey;

    public JwtKeys(List<JwtKey> keys) {
        this.allKeys = keys;
        this.activeSigningKey = keys.stream()
                .filter(this::isActive) 
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No active JWT key registered"));
    }

    public JwtKey activeKey() {
        return activeSigningKey;
    }

    public JwtKey keyForId(UUID kid) {
        return allKeys.stream()
                .filter(k -> k.kid().equals(kid))
                .findFirst()
                .orElseThrow(() -> notFoundException(kid));
    }

    private static SecurityException notFoundException(UUID kid) {
        return new SecurityException(JWT_KEY_RESOLVER_ERROR,                
                new NoSuchElementException("Key not found for kid: " + kid));
    }

    public JWKSet toJWKSet() {
        return new JWKSet(allKeys.stream().map(JwtKey::rsaKey).collect(Collectors.toUnmodifiableList()));
    }

    private boolean isActive(JwtKey key) {
        return "ACTIVE".equalsIgnoreCase(key.status());
    }
}