package dev.louisa.jam.hub.application.user.port.outbound;

import dev.louisa.jam.hub.domain.user.UserId;
import dev.louisa.jam.hub.infrastructure.security.jwt.provider.JwtCustomClaimBuilder;

public interface JwtProvider {
    
    String generate(UserId userId);
    
    String generate(UserId userId, JwtCustomClaimBuilder claimsBuilder);
}
