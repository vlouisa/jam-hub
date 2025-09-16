package dev.louisa.jam.hub.infrastructure.security.jwt.authenticator;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface JwtValidator {
    DecodedJWT validate(String token);
}