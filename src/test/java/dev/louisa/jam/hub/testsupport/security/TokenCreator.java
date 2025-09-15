package dev.louisa.jam.hub.testsupport.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import net.datafaker.Faker;

import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static dev.louisa.jam.hub.testsupport.security.TokenCustomizer.noCustomization;
import static dev.louisa.jam.hub.testsupport.security.TokenCustomizer.withExpires;
import static dev.louisa.jam.hub.infrastructure.security.util.RSAKeyReader.readPrivateKeyFromFile;
import static java.time.temporal.ChronoUnit.MINUTES;

public class TokenCreator {
    private static final RSAPrivateKey DEFAULT_PRIVATE_KEY = readPrivateKeyFromFile("jwk-set/19b14038-11df-43c5-a03c-db39a55b4e5b.key");
    private final Faker faker = new Faker();

    private RSAPrivateKey rsaPrivateKey = DEFAULT_PRIVATE_KEY;
    
    public static TokenCreator create() {
        return new TokenCreator();
    }
    
    public String anExpiredToken() {
        return anExpiredToken(noCustomization());
    }

    public String anExpiredToken(TokenCustomizer tokenCustomizer) {
        return aToken(tokenCustomizer.andThen(withExpires(Instant.now().minus(1, MINUTES))));
    }

    public String aToken() {
        return aToken(noCustomization());
    }

    public String aToken(TokenCustomizer tokenCustomizer) {
        final JWTCreator.Builder builder = JWT.create()
                .withSubject(UUID.randomUUID().toString())
                .withClaim("jam-hub:email", faker.internet().emailAddress())
                .withClaim("jam-hub:bands", List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
                .withIssuer("urn:jam-hub:auth")
                .withAudience("jam-hub-service");
        
        return sign(createToken(builder, tokenCustomizer));
    }

    // --- Switch into key selector mode ---
    public TokenCreator.KeyMode using(RSAPrivateKey rsaPrivateKey) {
        if (rsaPrivateKey == null) {
            throw new RuntimeException("RSAKey must not be null");
        }
        this.rsaPrivateKey = rsaPrivateKey;
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
                .sign(Algorithm.RSA256(this.rsaPrivateKey));
    }
}
