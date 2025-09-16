package dev.louisa.jam.hub.infrastructure.security.jwt.common;

import com.nimbusds.jose.jwk.*;
import dev.louisa.jam.hub.domain.common.Guard;
import dev.louisa.jam.hub.infrastructure.security.exception.SecurityException;
import dev.louisa.jam.hub.infrastructure.security.jwt.config.JwtProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.JWT_KEY_RESOLVER_ERROR;

@Slf4j
public class JwtKeys {
    private final List<JwtKey> jwtKeys = new ArrayList<>();
    private final JwtProperties jwtProperties;

    public JwtKeys(JwtProperties jwtProperties, JwtKey... jwtKeys) {
        this.jwtKeys.addAll(List.of(jwtKeys));
        this.jwtProperties = jwtProperties;
        logKeysInfo(this.jwtKeys);
        Guard.when(invalidActiveKeySpecified(jwtProperties.getActiveBundle(), jwtKeys))
                .thenThrow(() -> new NoSuchElementException("Invalid active key bundle specified [%s]".formatted(jwtProperties.getActiveBundle())));
    }

    private static boolean invalidActiveKeySpecified(String activeBundle, JwtKey[] jwtKeys) {
        return Arrays.stream(jwtKeys)
                .map(JwtKey::bundleName)
                .noneMatch(bundleName -> bundleName.equals(activeBundle));
    }

    private void logKeysInfo(List<JwtKey> keys) {
        log.info("---------------------------------- JWT KEYS -----------------------------");
        keys.stream()
                .map(jwtKey -> formatLine(jwtKey.bundleName(), jwtKey.kid()))
                .forEach(log::info);
        log.info("---------------------------------- JWT KEYS -----------------------------");
    }

    private String formatLine(String bundleName, String kid) {
        return String.format(" - %s (kid: %s)%s",
                bundleName,
                kid,
                bundleName.equals(jwtProperties.getActiveBundle()) ? " [ACTIVE]" : "");
    }

    public JwtKey activeKey() {
        return keyForBundle(jwtProperties.getActiveBundle());
    }

    public JwtKey keyForBundle(String bundleName) {
        try {
            return jwtKeys.stream()
                    .filter(jwtKey -> jwtKey.bundleName().equals(bundleName))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("No key found for bundle: " + bundleName));
        } catch (NoSuchElementException e) {
            throw new SecurityException(JWT_KEY_RESOLVER_ERROR, e);
        }
    }

    public JWKSet toJWKSet() {
        return new JWKSet(
                jwtKeys.stream()
                        .map(JwtKey::rsaKey)
                        .collect(Collectors.toUnmodifiableList()));
    }
}