package dev.louisa.jam.hub.infrastructure.security.jwt.common;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.Builder;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Builder
public record JwtKey(
        String kid, 
        String bundleName, 
        RSAKey rsaKey) {
    
    public RSAPublicKey toPublicKey() {
        try {
            return rsaKey.toRSAPublicKey();
        } catch (JOSEException e) {
            throw new RuntimeException("Failed to convert to RSAPublicKey", e);
        }
    }

    public RSAPrivateKey toPrivateKey() {   
        try {
            return rsaKey.toRSAPrivateKey();
        } catch (JOSEException e) {
            throw new RuntimeException("Failed to convert to RSAPrivateKey", e);
        }
    }
}
