package dev.louisa.jam.hub.domain.registration;

import dev.louisa.jam.hub.domain.shared.EmailAddress;
import dev.louisa.jam.hub.testsupport.BaseDomainTest;
import dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainException;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainError.*;
import static dev.louisa.jam.hub.testsupport.Factory.domain.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class UserRegistrationTest extends BaseDomainTest {
    private UserRegistration registration;


    @Test
    void shouldCreateNewRegistration() {
        var email = EmailAddress.builder()
                .email("elaine.marley@lechuck.be")
                .build();

        registration = UserRegistration.createNewRegistration(email);

        assertThat(registration.getId()).isNotNull();
        assertThat(registration.getEmail()).isEqualTo(email);
        assertThat(registration.getOtp()).isNotNull();
        assertThat(registration.getVerifiedAt()).isNull();
        assertThat(registration.getExpiredAt()).isAfter(Instant.now());
        assertThat(registration.getRevokedAt()).isNull();
    }

    @Test
    void shouldVerifyRegistration() {
        registration = aUserRegistration.create();

        registration.verify();

        assertThat(registration.getVerifiedAt()).isNotNull();
    }

    @Test
    void shouldThrowWhenVerifyingExpiredRegistration() {
        registration = aUserRegistration.createExpired();

        assertThatCode(() -> registration.verify())
                .isInstanceOf(UserRegistrationDomainException.class)
                .hasMessageContaining(OTP_CODE_EXPIRED.getMessage());
    }

    @Test
    void shouldThrowWhenVerifyingRevokedRegistration() {
        registration = aUserRegistration.createRevoked();

        assertThatCode(() -> registration.verify())
                .isInstanceOf(UserRegistrationDomainException.class)
                .hasMessageContaining(OTP_CODE_REVOKED.getMessage());
    }
}