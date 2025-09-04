package dev.louisa.jam.hub.interfaces.registration.persistence;

import dev.louisa.jam.hub.domain.registration.UserRegistration;
import dev.louisa.jam.hub.domain.registration.UserRegistrationId;

import java.util.Optional;

public interface UserRegistrationRepository {
    UserRegistration save(UserRegistration registration);
    
    Optional<UserRegistration> findById(UserRegistrationId userRegistrationId);
}
