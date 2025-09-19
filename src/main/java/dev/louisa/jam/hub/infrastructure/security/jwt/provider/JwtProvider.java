package dev.louisa.jam.hub.infrastructure.security.jwt.provider;

import dev.louisa.jam.hub.infrastructure.Clock;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    private final JwtEncoder jwtEncoder;
    private final JwtKeys jwtKeys;
    private final Clock clock;
    
    
    public String generate(UUID userId) {
        return generate(userId, JwtCustomClaimBuilder.customClaims());

    }
    public String generate(UUID userId, JwtCustomClaimBuilder customClaims) {
        final JwsHeader jwsHeaders = JwsHeader
                .with(SignatureAlgorithm.RS256)
                .keyId(jwtKeys.activeKey().kid().toString())
                .type("JWT")
                .build();

        final Instant now = clock.now();
        final JwtClaimsSet.Builder claims =
                JwtClaimsSet.builder()
                        .id(UUID.randomUUID().toString())
                        .subject(userId.toString())
                        .issuer("urn:jam-hub:auth")
                        .audience(List.of("jam-hub-service", "jam-hub-gateway"))
                        .issuedAt(now)
                        .notBefore(now)
                        .expiresAt(now.plus(10, MINUTES));
        
                customClaims.build().forEach(claims::claim);

        return sign(JwtEncoderParameters.from(jwsHeaders, claims.build()))
                .getTokenValue();
    }

    /**
     * Signs the JWT.
     * @param jwt the JWT to sign
     * @return the signed JWT
     * @throws JwtException if the JWT could not be signed
     * 
     * Encoder selects the correct private key based on the 'kid' in the header
     */
    private Jwt sign(JwtEncoderParameters jwt) {
        final String keyId = Objects.requireNonNull(jwt.getJwsHeader()).getKeyId(); 
        log.info("Key with kid '{}' used for signing token", keyId);
        return jwtEncoder
                .encode(jwt);
    }
}

