package dev.louisa.jam.hub.infrastructure.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class JwtKeySource {
    private final JwtKeys jwtKeys;
    
    public String getFirstKeyId() {
        return jwtKeys.getKeys()
                .keySet()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Map is empty"));
    }

    public RSAPublicKey getFirstPublicKey() {
        return jwtKeys.getKeys()
                .values()
                .stream()
                .findFirst()
                .map(JwtKey::rsaKey)
                .map(this::convertToRsaPublicKey)
                .orElseThrow(() -> new NoSuchElementException("No RSA Public Key found"));
    }

    public RSAPrivateKey getFirstPrivateKey() {
        return jwtKeys.getKeys()
                .values()
                .stream()
                .findFirst()
                .map(JwtKey::rsaKey)
                .map(this::convertToRsaPrivateKey)
                .orElseThrow(() -> new NoSuchElementException("No RSA Private Key found"));
    }

    public JWKSet toJWKSet() {
        return new JWKSet(new ArrayList<>(getKeys()));
    }

    private List<RSAKey> getKeys() {
        return jwtKeys.getKeys()
                .values()
                .stream()
                .map(JwtKey::rsaKey)
                .toList();
    }
    
    private RSAPublicKey convertToRsaPublicKey(JWK jwk) {
        try {
            return jwk.toRSAKey().toRSAPublicKey();
        } catch (JOSEException e) {
            throw new IllegalStateException("Failed to convert JWK to RSAPublicKey", e);
        }
    }

    private RSAPrivateKey convertToRsaPrivateKey(JWK jwk) {
        try {
            return jwk.toRSAKey().toRSAPrivateKey();
        } catch (JOSEException e) {
            throw new IllegalStateException("Failed to convert JWK to RSAPrivateKey", e);
        }
    }

}