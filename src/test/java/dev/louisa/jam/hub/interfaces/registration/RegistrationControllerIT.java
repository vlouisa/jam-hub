package dev.louisa.jam.hub.interfaces.registration;

import dev.louisa.jam.hub.domain.registration.persistence.UserRegistrationRepository;
import dev.louisa.jam.hub.infrastructure.ErrorResponse;
import dev.louisa.jam.hub.testsupport.BaseInterfaceIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainError.OTP_CODE_EXPIRED;
import static dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainError.OTP_CODE_REVOKED;
import static dev.louisa.jam.hub.infrastructure.exceptions.security.TokenCreator.*;
import static dev.louisa.jam.hub.testsupport.Factory.domain.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

class RegistrationControllerIT extends BaseInterfaceIT {
    @Autowired
    private UserRegistrationRepository userRegistrationRepository;

    @Test
    void shouldVerifyUserRegistration() throws Exception {
        var registration = aUserRegistration
                .usingRepository(userRegistrationRepository)
                .create();

        api.post("/api/v1/registrations/{registrationId}/verify", registration.getId().toValue())
                .withJwt(create().aDefaultToken())
                .expectResponseStatus(NO_CONTENT)
                .expectNoResponseBody();

    }

    @Test
    void shouldRespondInIdempotentWayWhenUserRegistrationAlreadyVerified() throws Exception {
        var registration = aUserRegistration
                .usingRepository(userRegistrationRepository)
                .createVerified();

        api.post("/api/v1/registrations/{registrationId}/verify", registration.getId().toValue())
                .withJwt(create().aDefaultToken())
                .expectResponseStatus(NO_CONTENT)
                .expectNoResponseBody();

        var retrievedRegistration = userRegistrationRepository.findById(registration.getId()).orElseThrow();
        assertThat(retrievedRegistration.getVerifiedAt()).isEqualTo(registration.getVerifiedAt());
    }

    @Test
    void shouldRespondWithBadRequestWhenUserRegistrationExpired() throws Exception {
        var registration = aUserRegistration
                .usingRepository(userRegistrationRepository)
                .createExpired();

        var response = api.post("/api/v1/registrations/{registrationId}/verify", registration.getId().toValue())
                .withJwt(create().aDefaultToken())
                .expectResponseStatus(BAD_REQUEST)
                .expectResponseBody(ErrorResponse.class);
        assertThat(response).isEqualTo(errorResponse(OTP_CODE_EXPIRED));
    }

    @Test
    void shouldRespondWithBadRequestWhenUserRegistrationRevoked() throws Exception {
        var registration = aUserRegistration
                .usingRepository(userRegistrationRepository)
                .createRevoked();

        var response = api.post("/api/v1/registrations/{registrationId}/verify", registration.getId().toValue())
                .withJwt(create().aDefaultToken())
                .expectResponseStatus(BAD_REQUEST)
                .expectResponseBody(ErrorResponse.class);
        assertThat(response).isEqualTo(errorResponse(OTP_CODE_REVOKED));
    }
}