package dev.louisa.jam.hub.infrastructure.security.jwt.authenticator;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.louisa.jam.hub.infrastructure.security.exception.SecurityException;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKey;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKeys;
import dev.louisa.jam.hub.testsupport.base.BaseInfraStructureTest;
import dev.louisa.jam.hub.testsupport.jwt.JwtKeyGenerator;
import dev.louisa.jam.hub.testsupport.security.TokenCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;


import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class JwtValidatorImplTest extends BaseInfraStructureTest {
    private JwtKey activeJwtKey;
    private JwtKey otherJwtKey;
    private JwtKey aJwtKey;
    private JwtKey aSpoofedJwtKey;

    private JwtValidatorImpl jwtValidator;

    
    @BeforeEach
    void setUp() throws Exception {
        final JwtKeyGenerator generator = new JwtKeyGenerator();
        activeJwtKey = generator.generate("6DlQrV5scz0r62JTSmipdEGRvZ9sSrtxZuTLY/nTsOE=");
        otherJwtKey = generator.generate("6DlQrV5scz0r62JTSmipdEGRvZ9sSrtxZuTLY/nTsOE=", "INACTIVE");
        aJwtKey = generator.generate("6DlQrV5scz0r62JTSmipdEGRvZ9sSrtxZuTLY/nTsOE=", "INACTIVE");
        
        aSpoofedJwtKey = JwtKey.builder()
                .kid(activeJwtKey.kid())    // Using the same kid as the active key to simulate a spoofed key
                .rsaKey(otherJwtKey.rsaKey()) 
                .build();
        jwtValidator = new JwtValidatorImpl(new JwtKeys(List.of(activeJwtKey, otherJwtKey)));
    }

    @Test
    void shouldValidateToken() {
        String token = TokenCreator.create()
                .using(activeJwtKey)
                .aToken();
        
        DecodedJWT decodedJWT = jwtValidator.validate(token);

        assertThat(decodedJWT)
                .isNotNull()
                .extracting(DecodedJWT::getPayload)
                .isNotNull();
    }

    @Test
    void shouldValidateWhenTokenHasBeenSignedWithOtherValidKey() {
        String token = TokenCreator.create()
                .using(otherJwtKey)
                .aToken();

        DecodedJWT decodedJWT = jwtValidator.validate(token);
        
        assertThat(decodedJWT)
                .isNotNull()
                .extracting(DecodedJWT::getPayload)
                .isNotNull();
    }

    @Test
    void shouldThrowWhenTokenHasBeenSignedWithUnregisteredKey() {
        String token = TokenCreator.create()
                .using(aJwtKey)
                .aToken();
        
        assertThatCode(() -> jwtValidator.validate(token))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining(JWT_KEY_RESOLVER_ERROR.getMessage())
                .hasRootCauseInstanceOf(NoSuchElementException.class)
                .hasRootCauseMessage("Key not found for kid: %s".formatted(aJwtKey.kid()));
    }

    @Test
    void shouldThrowWhenSpoofedTokenHasBeenUsed() {
        String token = TokenCreator.create()
                .using(aSpoofedJwtKey)
                .aToken();
        
        assertThatCode(() -> jwtValidator.validate(token))
                .isInstanceOf(JWTVerificationException.class)
                .hasMessageContaining("The Token's Signature resulted invalid when verified using the Algorithm: SHA256withRSA");
    }
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowWhenSubjectIsMissing(String subject) {
        String token = TokenCreator.create()
                .using(activeJwtKey)
                .aToken(t -> t.withSubject(subject));

        assertThatCode(() -> jwtValidator.validate(token))
                .isInstanceOf(JWTVerificationException.class)
                .hasMessage("The Claim 'sub' is missing or blank");

    }

    @Test
    void shouldThrowWhenSubjectIsNotUUIDv4() {
        String token = TokenCreator.create()
                .using(activeJwtKey)
                .aToken(t -> t.withSubject("not-a-uuid"));

        assertThatCode(() -> jwtValidator.validate(token))
                .isInstanceOf(JWTVerificationException.class)
                .hasMessage("The Claim 'sub' is not a UUID v4");
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"invalid-issuer", "urn:other-issuer"})
    void shouldThrowWhenIssuerIsNotValid(String issuer) {
        String token = TokenCreator.create()
                .using(activeJwtKey)
                .aToken(t -> t.withIssuer(issuer));

        assertThatCode(() -> jwtValidator.validate(token))
                .isInstanceOf(JWTVerificationException.class)
                .hasMessage("The Claim 'iss' value doesn't match the required issuer.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"invalid-audience"})
    void shouldThrowWhenAudienceNotValid(String audience) {
        String token = TokenCreator.create()
                .using(activeJwtKey)
                .aToken(t -> t.withAudience(audience));

        assertThatCode(() -> jwtValidator.validate(token))
                .isInstanceOf(JWTVerificationException.class)
                .hasMessage("The Claim 'aud' value doesn't contain the required audience.");
    }

    @Test
    void shouldThrowWhenJwtHasExpired() {
        String token = TokenCreator.create()
                .using(activeJwtKey)
                .anExpiredToken();

        assertThatCode(() -> jwtValidator.validate(token))
                .isInstanceOf(JWTVerificationException.class)
                .hasMessageContaining("The Token has expired on ");
    }

    @Test
    void shouldThrowWhenJwtIsNotYetValid() {
        String token = TokenCreator.create()
                .using(activeJwtKey)
                .aToken(t -> t.withNotBefore(Instant.now().plusSeconds(60)));

        assertThatCode(() -> jwtValidator.validate(token))
                .isInstanceOf(JWTVerificationException.class)
                .hasMessageContaining("The Token can't be used before ");
    }

    @Test
    void shouldThrowWhenJwtHasContainsAnUnexpectedClaim() {
        String token = TokenCreator.create()
                .using(activeJwtKey)
                .aToken(t -> t.withClaim("unexpected-claim", "GTA V for the win!"));

        assertThatCode(() -> jwtValidator.validate(token))
                .isInstanceOf(JWTVerificationException.class)
                .hasMessageContaining("The token contains non white-listed claims");
    }

}