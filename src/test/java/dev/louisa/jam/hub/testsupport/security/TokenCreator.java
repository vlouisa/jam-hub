package dev.louisa.jam.hub.testsupport.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.UUID;

import static dev.louisa.jam.hub.testsupport.security.TokenCustomizer.withExpires;
import static dev.louisa.jam.hub.infrastructure.security.util.RSAKeyReader.readPrivateKeyFromFile;
import static java.time.temporal.ChronoUnit.MINUTES;

public class TokenCreator {
    private static final RSAPrivateKey DEFAULT_PRIVATE_KEY = readPrivateKeyFromFile("jwk-set/19b14038-11df-43c5-a03c-db39a55b4e5b.key");
    
    private final RSAPrivateKey privateKey;
    
    public static TokenCreator create() {
        return new TokenCreator(DEFAULT_PRIVATE_KEY);
    }

    public static TokenCreator createUsing(RSAPrivateKey rsaPrivateKey) {
        return new TokenCreator(rsaPrivateKey);
    }
    
    private TokenCreator(RSAPrivateKey rsaPrivateKey) {
        this.privateKey = rsaPrivateKey;
    }

    public String aBlankToken(TokenCustomizer tokenCustomizer) {
        final JWTCreator.Builder builder = JWT.create();
        return sign(createToken(builder, tokenCustomizer));
    }


    public String anExpiredToken() {
        return anExpiredToken(token -> {});
    }

    public String anExpiredToken(TokenCustomizer tokenCustomizer) {
        return aDefaultToken(tokenCustomizer.andThen(withExpires(Instant.now().minus(1, MINUTES))));
    }

    public String aDefaultToken() {
        return aDefaultToken(token -> {});
    }

    public String aDefaultToken(TokenCustomizer tokenCustomizer) {
        final JWTCreator.Builder builder = JWT.create()
                .withSubject(UUID.randomUUID().toString())
                .withIssuer("jam-hub")
                .withAudience("jam-hub-service");
        
        return sign(createToken(builder, tokenCustomizer));
    }
    

    private JWTCreator.Builder createToken(JWTCreator.Builder jwtBuilder, TokenCustomizer tokenCustomizer) {
        JWTCreator.Builder builder =
                jwtBuilder
                        .withKeyId("19b14038-11df-43c5-a03c-db39a55b4e5b")
                        .withIssuedAt(Instant.now())
                        .withNotBefore(Instant.now())
                        .withExpiresAt(Instant.now().plus(10, MINUTES))
                        .withJWTId(UUID.randomUUID().toString());

        tokenCustomizer.accept(builder);
        return builder;
    }

    private String sign(JWTCreator.Builder customizedBuilder) {
        return customizedBuilder
                .sign(Algorithm.RSA256(this.privateKey));
    }
}
