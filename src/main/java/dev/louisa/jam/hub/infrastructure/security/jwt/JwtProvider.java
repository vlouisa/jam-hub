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
    private final JWKSourceService jwkSourceService;

    public String generate(UUID userId, List<UUID> bandIds, EmailAddress emailAddress) {
        final Instant now = Instant.now();

        final JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .subject(userId.toString())
                .claim("email", emailAddress.address())
                .claim("bands", bandIds)
                .issuer("jam-hub")
                .issuedAt(now)
                .notBefore(now)
                .expiresAt(now.plus(4, MINUTES));


        final JwtClaimsSet claims = claimsBuilder.build();

        final JwsHeader jwsHeaders = JwsHeader
                .with(SignatureAlgorithm.RS256)
                .keyId(jwkSourceService.getKidFromFirstSigningKey())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeaders, claims)).getTokenValue();
    }
}

