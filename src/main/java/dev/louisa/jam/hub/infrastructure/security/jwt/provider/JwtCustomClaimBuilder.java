package dev.louisa.jam.hub.infrastructure.security.jwt.provider;

import dev.louisa.jam.hub.domain.common.Guard;
import dev.louisa.jam.hub.infrastructure.security.exception.SecurityException;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.JWT_CLAIM_BUILDER_RESERVED_CLAIM;

public class JwtCustomClaimBuilder {
    private static final List<String> RESERVED_CLAIMS = List.of("iss", "sub", "aud", "exp", "nbf", "iat", "jti");

    private final Map<String, Object> claims = new LinkedHashMap<>();

    public static JwtCustomClaimBuilder customClaims() {
        return new JwtCustomClaimBuilder();
    }

    public JwtCustomClaimBuilder singleValue(String name, Object value) {
        validateClaimName(name);
        claims.put(name, value);
        return this;
    }

    public JwtCustomClaimBuilder multiValue(String name, List<?> values) {
        validateClaimName(name);
        claims.put(name, values);
        return this;
    }

    private void validateClaimName(String name) {
        try {
            Guard.when(name == null || name.isBlank()).thenThrow(new IllegalArgumentException("Claim name must not be null or blank"))
                    .orWhen(isReserved(name)).thenThrow(new IllegalArgumentException("Claim name '%s' is reserved".formatted(name)));
        } catch (IllegalArgumentException e) {
            throw new SecurityException(JWT_CLAIM_BUILDER_RESERVED_CLAIM, e);
        }
    }

    private boolean isReserved(String name) {
        return RESERVED_CLAIMS.contains(name.toLowerCase());
    }


    public Map<String, Object> build() {
        return Collections.unmodifiableMap(claims);
    }
}