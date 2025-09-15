package dev.louisa.jam.hub.infrastructure.security.jwt;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class JwtKeys {
    private final Map<String, JwtKey> keys = new HashMap<>();
    
    public JwtKeys(JwtKey... jwtKeys) {
        for (JwtKey key : jwtKeys) {
            keys.put(key.kid(), key);
        }
    }
}
