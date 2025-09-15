package dev.louisa.jam.hub.infrastructure.security.jwt;

import dev.louisa.jam.hub.infrastructure.mail.EmailAddress;
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
    private final JwtKeySource jwtKeySource;

    public String generate(UUID userId, List<UUID> bandIds, EmailAddress emailAddress) {
        final JwsHeader jwsHeaders = JwsHeader
                .with(SignatureAlgorithm.RS256)
                .keyId(jwtKeySource.getFirstKeyId())
                .type("JWT")
                .build();

        final JwtClaimsSet claims =
                JwtClaimsSet.builder()
                        .id(UUID.randomUUID().toString())
                        .subject(userId.toString())
                        .issuer("urn:jam-hub:auth")
                        .audience(List.of("jam-hub-service"))
                        .claim("jam-hub:email", emailAddress.address())
                        .claim("jam-hub:bands", bandIds)
                        .issuedAt(Instant.now())
                        .notBefore(Instant.now())
                        .expiresAt(Instant.now().plus(10, MINUTES)).build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(jwsHeaders, claims))
                .getTokenValue();
    }
}

