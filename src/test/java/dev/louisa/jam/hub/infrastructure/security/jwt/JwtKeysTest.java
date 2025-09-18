package dev.louisa.jam.hub.infrastructure.security.jwt;

import com.nimbusds.jose.jwk.RSAKey;
import dev.louisa.jam.hub.infrastructure.security.exception.SecurityException;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKey;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKeys;
import dev.louisa.jam.hub.infrastructure.security.jwt.config.JwtProperties;
import dev.louisa.jam.hub.testsupport.base.BaseInfraStructureTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.NoSuchElementException;

import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class JwtKeysTest extends BaseInfraStructureTest {
    private JwtProperties jwtProperties;

    @Mock
    private RSAKey key202501;
    @Mock
    private RSAKey key202502;
    @Mock
    private RSAKey key202503;

    private JwtKeys jwtKeys;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
        jwtProperties.setActiveBundle("2025.02");
    }

    @Test
    void shouldLoadKeys() {
        jwtKeys = new JwtKeys(
                jwtProperties,
                new JwtKey("0dab6fda-b954-4b2b-ba55-2160cc53b94c", "2025.01", key202501),
                new JwtKey("1e3b6fda-b954-4b2b-ba55-2160cc53b94c", "2025.02", key202502),
                new JwtKey("2f4c6fda-b954-4b2b-ba55-2160cc53b94c", "2025.03", key202503)
        );

        assertThat(jwtKeys.keyForBundle("2025.01").rsaKey()).isEqualTo(key202501);
        assertThat(jwtKeys.keyForBundle("2025.02").rsaKey()).isEqualTo(key202502);
        assertThat(jwtKeys.keyForBundle("2025.03").rsaKey()).isEqualTo(key202503);
        assertThat(jwtKeys.activeKey().rsaKey()).isEqualTo(key202502);
        assertThat(jwtKeys.toJWKSet().getKeys().size()).isEqualTo(3);
    }

    @Test
    void shouldThrowWhenActiveKeyIsNotPresent() {
        jwtProperties.setActiveBundle("2024.99");

        assertThatCode(() ->
                new JwtKeys(
                        jwtProperties,
                        new JwtKey("0dab6fda-b954-4b2b-ba55-2160cc53b94c", "2025.01", key202501)))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Invalid active key bundle specified [2024.99]");
    }

    @Test
    void shouldThrowWhenRequestedKeyIsNotPresent() {
        jwtProperties.setActiveBundle("2025.01");
        jwtKeys = new JwtKeys(
                jwtProperties,
                new JwtKey("0dab6fda-b954-4b2b-ba55-2160cc53b94c", "2025.01", key202501)
        );

        assertThatCode(() -> jwtKeys.keyForBundle("2025.02"))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining(JWT_KEY_RESOLVER_ERROR.getMessage())
                .hasRootCauseInstanceOf(NoSuchElementException.class)
                .hasRootCauseMessage("No key found for bundle: 2025.02")
        ;
    }
}