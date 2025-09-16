package dev.louisa.jam.hub.infrastructure.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtEncoder jwtEncoder;
    private final JwtKeys jwtKeys;

    public String generate(UUID userId, JwtCustomClaimBuilder customClaims) {
        final JwsHeader jwsHeaders = JwsHeader
                .with(SignatureAlgorithm.RS256)
                .keyId(jwtKeys.activeKey().kid())
                .type("JWT")
                .build();

        final JwtClaimsSet.Builder claims =
                JwtClaimsSet.builder()
                        .id(UUID.randomUUID().toString())
                        .subject(userId.toString())
                        .issuer("urn:jam-hub:auth")
                        .audience(List.of("jam-hub-service"))
                        .issuedAt(Instant.now())
                        .notBefore(Instant.now())
                        .expiresAt(Instant.now().plus(10, MINUTES));
        
                customClaims.build().forEach(claims::claim);        
        
        return jwtEncoder
                .encode(JwtEncoderParameters.from(jwsHeaders, claims.build()))
                .getTokenValue();
    }
}

