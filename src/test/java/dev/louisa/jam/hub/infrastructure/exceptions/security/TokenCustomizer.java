package dev.louisa.jam.hub.infrastructure.exceptions.security;

import com.auth0.jwt.JWTCreator;

import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface TokenCustomizer extends Consumer<JWTCreator.Builder> {

    default TokenCustomizer andThen(TokenCustomizer after) {
        Objects.requireNonNull(after);
        return (customizer) -> {
            accept(customizer);
            after.accept(customizer);
        };
    }

    static TokenCustomizer noCustomization() {
        return (customizer) -> {};
    }

    static TokenCustomizer withSubject(String subject) {
        return jwt -> jwt.withSubject(subject);
    }

    static TokenCustomizer withIssuer(String issuer) {
        return jwt -> jwt.withIssuer(issuer);
    }

    static TokenCustomizer withAudience(String ... audience) {
        return jwt -> jwt.withAudience(audience);
    }

    static TokenCustomizer withNotBefore(Instant instant) {
        return jwt -> jwt.withNotBefore( instant);
    }

    static TokenCustomizer withIssuedAt(Instant instant) {
        return jwt -> jwt.withIssuedAt( instant);
    }

    static TokenCustomizer withExpires(Instant instant) {
        return jwt -> jwt.withExpiresAt( instant);
    }

    static TokenCustomizer withKeyId(String kid) {
        return jwt -> jwt.withKeyId( kid);
    }

    static TokenCustomizer withJwtId(String jwtId) {
        return jwt -> jwt.withJWTId( jwtId);
    }

    static TokenCustomizer withNull(String claim) {
        return jwt -> jwt.withNullClaim( claim);
    }
}
