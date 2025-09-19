package dev.louisa.jam.hub.infrastructure.security.jwt.authenticator;

import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKeys;
import dev.louisa.jam.hub.testsupport.base.BaseInfraStructureTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JwtValidatorImplTest extends BaseInfraStructureTest {

    private JwtKeys jwtKeys;

    private JwtValidatorImpl jwtValidator;

    
    //TODO: Fix this
//    @BeforeEach
//    void setUp() {
//
//        jwtKeys = new JwtKeys(List.of(JwtKeyCreator.fromBundle("2025.09.17.170803")));
//        jwtValidator = new JwtValidatorImpl(jwtKeys);
//    }
//
//    @Test
//    void shouldValidateToken() {
//        String token = TokenCreator.create()
//                .using(jwtKeys.activeKey())
//                .aToken();
//
//        DecodedJWT decodedJWT = jwtValidator.validate(token);
//
//        assertThat(decodedJWT)
//                .isNotNull()
//                .extracting(DecodedJWT::getPayload)
//                .isNotNull();
//    }
//
//    @ParameterizedTest
//    @NullAndEmptySource
//    void shouldThrowWhenSubjectIsMissing(String subject) {
//        String token = TokenCreator.create()
//                .using(jwtKeys.activeKey())
//                .aToken(t -> t.withSubject(subject));
//
//        assertThatCode(() -> jwtValidator.validate(token))
//                .isInstanceOf(JWTVerificationException.class)
//                .hasMessage("The Claim 'sub' is missing or blank");
//
//    }
//
//    @Test
//    void shouldThrowWhenSubjectIsNotUUIDv4() {
//        String token = TokenCreator.create()
//                .using(jwtKeys.activeKey())
//                .aToken(t -> t.withSubject("monkey-island"));
//
//        assertThatCode(() -> jwtValidator.validate(token))
//                .isInstanceOf(JWTVerificationException.class)
//                .hasMessage("The Claim 'sub' is not a UUID v4");
//    }
//
//    @ParameterizedTest
//    @NullAndEmptySource
//    @ValueSource(strings = {"invalid-issuer", "urn:other-issuer"})
//    void shouldThrowWhenIssuerIsNotValid(String issuer) {
//        String token = TokenCreator.create()
//                .using(jwtKeys.activeKey())
//                .aToken(t -> t.withIssuer(issuer));
//
//        assertThatCode(() -> jwtValidator.validate(token))
//                .isInstanceOf(JWTVerificationException.class)
//                .hasMessage("The Claim 'iss' value doesn't match the required issuer.");
//    }
//
//    @ParameterizedTest
//    @NullAndEmptySource
//    @ValueSource(strings = {"invalid-audience"})
//    void shouldThrowWhenAudienceNotValid(String audience) {
//        String token = TokenCreator.create()
//                .using(jwtKeys.activeKey())
//                .aToken(t -> t.withAudience(audience));
//
//        assertThatCode(() -> jwtValidator.validate(token))
//                .isInstanceOf(JWTVerificationException.class)
//                .hasMessage("The Claim 'aud' value doesn't contain the required audience.");
//    }
//
//    @Test
//    void shouldThrowWhenJwtHasExpired() {
//        String token = TokenCreator.create()
//                .using(jwtKeys.activeKey())
//                .anExpiredToken();
//
//        assertThatCode(() -> jwtValidator.validate(token))
//                .isInstanceOf(JWTVerificationException.class)
//                .hasMessageContaining("The Token has expired on ");
//    }
//
//    @Test
//    void shouldThrowWhenJwtIsNotYetValid() {
//        String token = TokenCreator.create()
//                .using(jwtKeys.activeKey())
//                .aToken(t -> t.withNotBefore(Instant.now().plusSeconds(60)));
//
//        assertThatCode(() -> jwtValidator.validate(token))
//                .isInstanceOf(JWTVerificationException.class)
//                .hasMessageContaining("The Token can't be used before ");
//    }
//
//    @Test
//    void shouldThrowWhenJwtHasContainsAnUnexpectedClaim() {
//        String token = TokenCreator.create()
//                .using(jwtKeys.activeKey())
//                .aToken(t -> t.withClaim("unexpected-claim", "GTA V for the win!"));
//
//        assertThatCode(() -> jwtValidator.validate(token))
//                .isInstanceOf(JWTVerificationException.class)
//                .hasMessageContaining("The token contains non white-listed claims");
//    }

}