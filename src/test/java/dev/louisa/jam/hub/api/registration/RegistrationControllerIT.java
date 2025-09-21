package dev.louisa.jam.hub.api.registration;

import dev.louisa.jam.hub.domain.registration.UserRegistrationId;
import dev.louisa.jam.hub.domain.registration.VerifyRegistrationRequest;
import dev.louisa.jam.hub.application.registration.port.outbound.UserRegistrationRepository;
import dev.louisa.jam.hub.infrastructure.ErrorResponse;
import dev.louisa.jam.hub.api.common.IdResponse;
import dev.louisa.jam.hub.testsupport.base.BaseApiIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static dev.louisa.jam.hub.application.exceptions.ApplicationError.*;
import static dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainError.*;
import static dev.louisa.jam.hub.testsupport.Factory.domain.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

class RegistrationControllerIT extends BaseApiIT {
    
    @Autowired
    private UserRegistrationRepository userRegistrationRepository;

    @Test
    void shouldCreateUserRegistration() throws Exception {
        var idResponse =
                api.post("/api/v1/registrations")
                        .body(RegistrationRequest.builder()
                                .email("guybrush.threepwood@lucas-arts.com")
                                .build())
                        .expectResponseStatus(CREATED)
                        .send()
                        .andReturn(IdResponse.class);
        
        var retrievedRegistration = userRegistrationRepository.findById(UserRegistrationId.fromUUID(idResponse.id())).orElseThrow();
        assertThat(retrievedRegistration.getEmail().email()).isEqualTo("guybrush.threepwood@lucas-arts.com");
    }

    @Test
    void shouldVerifyUserRegistration() throws Exception {
        var registration = aUserRegistration
                .usingRepository(userRegistrationRepository)
                .create();

        api.post("/api/v1/registrations/{registrationId}/verify", registration.getId().toValue())
                .body(VerifyRegistrationRequest.builder()
                        .otp(registration.getOtp())
                        .password("A_strong_password1!")
                        .build())
                .expectResponseStatus(NO_CONTENT)
                .send();
        
        var retrievedRegistration = userRegistrationRepository.findById(registration.getId()).orElseThrow();
        assertThat(retrievedRegistration.getVerifiedAt()).isNotNull();
    }

    @Test
    void shouldRespondNotFoundWhenUserRegistrationDoesNotExist() throws Exception {
        var response = api.post("/api/v1/registrations/{registrationId}/verify",  UUID.randomUUID())
                .body(VerifyRegistrationRequest.builder()
                        .otp(UUID.randomUUID())
                        .password("A_strong_password1!")
                        .build())
                .expectResponseStatus(NOT_FOUND)
                .send()
                .andReturn(ErrorResponse.class);
        
        assertThat(response).isEqualTo(errorResponse(ENTITY_NOT_FOUND));
    }

    @Test
    void shouldRespondWithConflictWhenUserRegistrationAlreadyVerified() throws Exception {
        var registration = aUserRegistration
                .usingRepository(userRegistrationRepository)
                .createVerified();

        var response = api.post("/api/v1/registrations/{registrationId}/verify",  registration.getId().toValue())
                .body(VerifyRegistrationRequest.builder()
                        .otp(registration.getOtp())
                        .password("A_strong_password1!")
                        .build())
                .expectResponseStatus(CONFLICT)
                .send()
                .andReturn(ErrorResponse.class);
        
        assertThat(response).isEqualTo(errorResponse(OTP_CODE_ALREADY_VERIFIED));
    }

    @Test
    void shouldRespondWithBadRequestWhenUserRegistrationExpired() throws Exception {
        var registration = aUserRegistration
                .usingRepository(userRegistrationRepository)
                .createExpired();

        var response = api.post("/api/v1/registrations/{registrationId}/verify",  registration.getId().toValue())
                .body(VerifyRegistrationRequest.builder()
                        .otp(registration.getOtp())
                        .password("A_strong_password1!")
                        .build())
                .expectResponseStatus(BAD_REQUEST)
                .send()
                .andReturn(ErrorResponse.class);

        assertThat(response).isEqualTo(errorResponse(OTP_CODE_EXPIRED));
    }

    @Test
    void shouldRespondWithBadRequestWhenUserRegistrationRevoked() throws Exception {
        var registration = aUserRegistration
                .usingRepository(userRegistrationRepository)
                .createRevoked();

        var response = api.post("/api/v1/registrations/{registrationId}/verify", registration.getId().toValue())
                .body(VerifyRegistrationRequest.builder()
                        .otp(registration.getOtp())
                        .password("A_strong_password1!")
                        .build())
                .expectResponseStatus(BAD_REQUEST)
                .send()
                .andReturn(ErrorResponse.class);

        assertThat(response).isEqualTo(errorResponse(OTP_CODE_REVOKED));
    }
}