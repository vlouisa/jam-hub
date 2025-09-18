package dev.louisa.jam.hub.infrastructure.security.jwt.authenticator;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKey;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKeys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

import static com.auth0.jwt.algorithms.Algorithm.RSA256;

@Component
@AllArgsConstructor
@Slf4j
public class JwtValidatorImpl implements JwtValidator {
    private static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");
    private static final List<String> WHITE_LISTED_CLAIMS = List.of("iss", "sub", "aud", "exp", "nbf", "iat", "jti", "jam-hub:bands");
    private final JwtKeys jwtKeys;

    public DecodedJWT validate(String token) {

        final JwtKey jwtKey = getVerificationKey(token);
        DecodedJWT jwt = JWT.require(RSA256(jwtKey.toPublicKey()))
                .withIssuer("urn:jam-hub:auth")
                .withAnyOfAudience("jam-hub-service")
                .build()
                .verify(token);

        validateClaims(jwt);

        return jwt;
    }

    private JwtKey getVerificationKey(String token) {
        final DecodedJWT unverifiedJwt = JWT.decode(token);
        final String keyId = unverifiedJwt.getKeyId();
        log.info("Key with kid '{}' used for verifying token", keyId);
        return jwtKeys.keyForId(keyId);
    }

    private void validateClaims(DecodedJWT jwt) {
        if (jwt.getSubject() == null || jwt.getSubject().isBlank()) {
            throw new JWTVerificationException("The Claim 'sub' is missing or blank");
        }

        if (!UUID_REGEX.matcher(jwt.getSubject()).matches()) {
            throw new JWTVerificationException("The Claim 'sub' is not a UUID v4");
        }

        if (jwt.getClaims().keySet().stream()
                .anyMatch(JwtValidatorImpl::isNotWhiteListed)) {
            throw new JWTVerificationException("The token contains non white-listed claims");
        }
    }

    private static boolean isNotWhiteListed(String claim) {
        return !WHITE_LISTED_CLAIMS.contains(claim);
    }
}
