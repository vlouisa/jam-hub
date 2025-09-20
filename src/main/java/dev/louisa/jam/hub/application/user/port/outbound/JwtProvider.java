package dev.louisa.jam.hub.application.user.port.outbound;

import dev.louisa.jam.hub.infrastructure.security.jwt.provider.JwtCustomClaimBuilder;

import java.util.UUID;

public interface JwtProvider {
    
    String generate(UUID userId);
    
    String generate(UUID userId, JwtCustomClaimBuilder claimsBuilder);
}
