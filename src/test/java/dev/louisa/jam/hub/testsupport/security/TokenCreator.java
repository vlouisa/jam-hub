package dev.louisa.jam.hub.testsupport.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKey;
import net.datafaker.Faker;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static dev.louisa.jam.hub.testsupport.security.TokenCustomizer.noCustomization;
import static dev.louisa.jam.hub.testsupport.security.TokenCustomizer.withExpires;
import static java.time.temporal.ChronoUnit.MINUTES;

public class TokenCreator {
    private JwtKey jwtKey;
    
    public static TokenCreator create() {
        return new TokenCreator();
    }
    
    private String anExpiredToken() {
        return anExpiredToken(noCustomization());
    }

    private String anExpiredToken(TokenCustomizer tokenCustomizer) {
        return aToken(tokenCustomizer.andThen(withExpires(Instant.now().minus(1, MINUTES))));
    }

    private String aToken() {
        return aToken(noCustomization());
    }

    private String aToken(TokenCustomizer tokenCustomizer) {
        final JWTCreator.Builder builder = JWT.create()
                .withSubject(UUID.randomUUID().toString())
                .withClaim("jam-hub:bands", List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
                .withIssuer("urn:jam-hub:auth")
                .withAudience("jam-hub-service");
        
        return sign(createToken(builder, tokenCustomizer));
    }

    // --- Switch into key selector mode ---
    public TokenCreator.KeyMode using(JwtKey jwtKey) {
        if (jwtKey == null) {
            throw new RuntimeException("JwtKey must not be null");
        }
        this.jwtKey = jwtKey;
        return new TokenCreator.KeyMode();
    }
    
    public class KeyMode{
        
        public String aToken() {
            return TokenCreator.this.aToken();
        }
        public String aToken(TokenCustomizer tokenCustomizer) {
            return TokenCreator.this.aToken(tokenCustomizer);
        }
        public String anExpiredToken() {
            return TokenCreator.this.anExpiredToken();
        }

        public String anExpiredToken(TokenCustomizer tokenCustomizer) {
            return TokenCreator.this.anExpiredToken(tokenCustomizer);
        }
    }
    
    private JWTCreator.Builder createToken(JWTCreator.Builder jwtBuilder, TokenCustomizer tokenCustomizer) {
        JWTCreator.Builder builder =
                jwtBuilder
                        .withKeyId(jwtKey.kid().toString())
                        .withIssuedAt(Instant.now())
                        .withNotBefore(Instant.now())
                        .withExpiresAt(Instant.now().plus(10, MINUTES))
                        .withJWTId(UUID.randomUUID().toString());

        tokenCustomizer.accept(builder);
        return builder;
    }

    private String sign(JWTCreator.Builder customizedBuilder) {
        return customizedBuilder
                .sign(Algorithm.RSA256(jwtKey.toPrivateKey()));
    }
}
