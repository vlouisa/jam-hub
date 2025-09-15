package dev.louisa.jam.hub.infrastructure.security.jwt;

import com.nimbusds.jose.jwk.RSAKey;
import lombok.Builder;

@Builder
public record JwtKey(String kid, RSAKey rsaKey) {}
