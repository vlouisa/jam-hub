package dev.louisa.jam.hub.infrastructure.security.jwt;

import com.nimbusds.jose.jwk.RSAKey;
import dev.louisa.jam.hub.infrastructure.security.exception.SecurityException;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKey;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKeys;
import dev.louisa.jam.hub.testsupport.base.BaseInfraStructureTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.JWT_KEY_RESOLVER_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@ExtendWith(SpringExtension.class)
class JwtKeysTest extends BaseInfraStructureTest {

    @Mock
    private RSAKey key202501;
    @Mock
    private RSAKey key202502;
    @Mock
    private RSAKey key202503;

    private JwtKeys jwtKeys;

    @Test
    void shouldLoadKeys() {
        jwtKeys = new JwtKeys(
               List.of(
                new JwtKey(UUID.fromString("0dab6fda-b954-4b2b-ba55-2160cc53b94c"), "INACTIVE", key202501),
                new JwtKey(UUID.fromString("1e3b6fda-b954-4b2b-ba55-2160cc53b94c"), "ACTIVE", key202502),
                new JwtKey(UUID.fromString("2f4c6fda-b954-4b2b-ba55-2160cc53b94c"), "INACTIVE", key202503)
               ));

        assertThat(jwtKeys.activeKey().rsaKey()).isEqualTo(key202502);
        assertThat(jwtKeys.toJWKSet().getKeys().size()).isEqualTo(3);
    }

    @Test
    void shouldThrowWhenActiveKeyIsNotPresent() {

        assertThatCode(() ->
                new JwtKeys(
                        List.of(new JwtKey(UUID.fromString("0dab6fda-b954-4b2b-ba55-2160cc53b94c"), "INACTIVE", key202501))))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No active JWT key registered");
    }

    @Test
    void shouldThrowWhenRequestedKeyIsNotPresent() {
        jwtKeys = new JwtKeys(
                List.of(new JwtKey(UUID.fromString("0dab6fda-b954-4b2b-ba55-2160cc53b94c"), "ACTIVE", key202501)));

        assertThatCode(() -> jwtKeys.keyForId(UUID.fromString("1e3b6fda-b954-4b2b-ba55-2160cc53b94c")))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining(JWT_KEY_RESOLVER_ERROR.getMessage())
                .hasRootCauseInstanceOf(NoSuchElementException.class)
                .hasRootCauseMessage("Key not found for kid: %s".formatted("1e3b6fda-b954-4b2b-ba55-2160cc53b94c"));
    }
}