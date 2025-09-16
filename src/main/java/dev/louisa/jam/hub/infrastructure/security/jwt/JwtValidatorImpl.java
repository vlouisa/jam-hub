package dev.louisa.jam.hub.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static com.auth0.jwt.algorithms.Algorithm.RSA256;

@Component
@AllArgsConstructor
public class JwtValidatorImpl implements JwtValidator {
    private static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");
    private final JwtKeys jwtKeys;
    
    public DecodedJWT validate(String token) {
        final JwtKey jwtKey = jwtKeys.activeKey();
        DecodedJWT jwt = JWT.require(RSA256(jwtKey.toPublicKey()))
                .withIssuer("urn:jam-hub:auth")
                .withAnyOfAudience("jam-hub-service")
                .build()
                .verify(token);

        validateClaims(jwt);

        return jwt;
    }
    
    private void validateClaims(DecodedJWT jwt) {
        if (jwt.getSubject() == null || jwt.getSubject().isBlank()) {
            throw new JWTVerificationException("The Claim 'sub' is missing or blank");
        }

        if (!UUID_REGEX.matcher(jwt.getSubject()).matches()) {
            throw new JWTVerificationException("The Claim 'sub' is not a UUID v4");
        }
    }
}
