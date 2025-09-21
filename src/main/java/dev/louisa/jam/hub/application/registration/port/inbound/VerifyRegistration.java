package dev.louisa.jam.hub.application.registration.port.inbound;

import dev.louisa.jam.hub.domain.registration.UserRegistrationId;

import java.util.UUID;

public interface VerifyRegistration {

    void verify(UserRegistrationId userRegistrationId, UUID otp, String rawPassword);
}
