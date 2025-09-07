package dev.louisa.jam.hub.infrastructure.security.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface JwtValidator {
    DecodedJWT validate(String token);
}