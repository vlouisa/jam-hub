package dev.louisa.jam.hub.application.auth;

import dev.louisa.jam.hub.application.auth.port.outbound.PasswordHasher;
import dev.louisa.jam.hub.domain.user.HashedPassword;
import dev.louisa.jam.hub.infrastructure.security.auth.BCryptPasswordHasher;
import dev.louisa.jam.hub.infrastructure.security.exception.SecurityException;
import dev.louisa.jam.hub.testsupport.base.BaseApplicationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static dev.louisa.jam.hub.application.auth.Password.fromString;
import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class PasswordTest extends BaseApplicationTest {

    private PasswordHasher passwordHasher;

    @BeforeEach
    void setUp() {
        passwordHasher = new BCryptPasswordHasher();
    }
    
    @Test
    void shouldConstructValidPasswordFromString() {
        String rawPassword = "M!ghtyP1r@te";
        Password password = fromString(rawPassword);

        assertThat(password.value())
                .isEqualTo(rawPassword);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Short1!",
            "nouppercase1!",
            "NOLOWERCASE1!",
            "NoSpecialChar1",
            "NoDigit!@#",
    })
    void shouldThrowWhenPasswordFromStringDoesNotMeetRequirements(String rawPassword) {
        assertThatCode(() -> fromString(rawPassword))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining(INVALID_CREDENTIALS.getMessage())
                .hasRootCauseMessage("Password does not meet complexity requirements");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowWhenPasswordFromStringIsBlankOrNull(String rawPassword) {
        assertThatCode(() -> fromString(rawPassword))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining(INVALID_CREDENTIALS.getMessage())
                .hasRootCauseMessage("Password cannot be empty");
    }

    @Test
    void shouldInformWhenPasswordMatches() {
        HashedPassword hashedPassword = hashedPassword("M!ghtyP1r@te");
        assertThat(Password.fromString("M!ghtyP1r@te").matches(hashedPassword, passwordHasher))
                .isTrue();
    }

    @Test
    void shouldInformWhenPasswordDoesNotMatch() {
        HashedPassword hashedPassword = hashedPassword("L3ChucksRevenge!");
        assertThat(Password.fromString("M!ghtyP1r@te").matches(hashedPassword, passwordHasher))
                .isFalse();
    }

    @Test
    void shouldConvertPasswordToHashedPassword() {
        HashedPassword hashedPasswordFromString = Password.fromString("M!ghtyP1r@te").hash(passwordHasher);
        assertThat(Password.fromString("M!ghtyP1r@te").matches(hashedPasswordFromString, passwordHasher))
                .isTrue();
    }

    @Test
    void shouldNotLeakPasswordViaToString() {
        assertThat(Password.fromString("M!ghtyP1r@te").toString())
                .isEqualTo("********");
    }

    private HashedPassword hashedPassword(String rawPassword) {
        return HashedPassword.fromString(passwordHasher.hash(rawPassword));
    }
}