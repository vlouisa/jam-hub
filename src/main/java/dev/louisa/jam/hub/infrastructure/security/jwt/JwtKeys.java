package dev.louisa.jam.hub.infrastructure.security.jwt;

import com.nimbusds.jose.jwk.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
public class JwtKeys {
    private final List<JwtKey> jwtKeys = new ArrayList<>();
    private final JwtProperties jwtProperties;
    
    public JwtKeys(JwtProperties jwtProperties, JwtKey... jwtKeys) {
        this.jwtKeys.addAll(List.of(jwtKeys));
        this.jwtProperties = jwtProperties;
        logKeysInfo(this.jwtKeys);
    
    }

    private void logKeysInfo(List<JwtKey> keys) {
        log.info("---------------------------------- JWT KEYS -----------------------------");
        keys.stream()
                .map(jwtKey -> formatLine(jwtKey.bundleName(),jwtKey.kid()))
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
        return jwtKeys.stream()
                .filter(jwtKey -> jwtKey.bundleName().equals(bundleName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No key found for bundle: " + bundleName));
    }

    public JWKSet toJWKSet() {
        return new JWKSet(
                jwtKeys.stream()
                        .map(JwtKey::rsaKey)
                        .collect(Collectors.toUnmodifiableList()));
    }
}