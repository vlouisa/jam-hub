package dev.louisa.jam.hub.infrastructure.security.jwt.common;

import com.nimbusds.jose.jwk.RSAKey;
import dev.louisa.jam.hub.infrastructure.security.exception.SecurityException;
import lombok.Builder;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.Callable;

import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.JWT_KEY_CONVERSION_ERROR;

@Builder
public record JwtKey(
        String kid, 
        String bundleName, 
        RSAKey rsaKey) {

    public RSAPublicKey toPublicKey() {
        return convertKey(rsaKey::toRSAPublicKey);
    }

    public RSAPrivateKey toPrivateKey() {
        return convertKey(rsaKey::toRSAPrivateKey);
    }   

    private <T> T convertKey(Callable<T> converter) {
        try {
            return converter.call();
        } catch (Exception e) {
            throw new SecurityException(JWT_KEY_CONVERSION_ERROR, e);
        }
    }
}
