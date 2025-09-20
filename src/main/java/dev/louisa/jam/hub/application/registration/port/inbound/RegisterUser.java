package dev.louisa.jam.hub.application.registration.port.inbound;

import dev.louisa.jam.hub.domain.common.EmailAddress;
import dev.louisa.jam.hub.domain.registration.UserRegistrationId;

public interface RegisterUser {
    
    UserRegistrationId register(EmailAddress emailAddress);
}
