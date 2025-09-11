package dev.louisa.jam.hub.domain.registration.persistence;

import dev.louisa.jam.hub.domain.registration.UserRegistration;
import dev.louisa.jam.hub.domain.registration.UserRegistrationId;

import java.util.Optional;
import java.util.UUID;

public interface UserRegistrationRepository {
    UserRegistration save(UserRegistration registration);
    
    Optional<UserRegistration> findById(UserRegistrationId userRegistrationId);
    Optional<UserRegistration> findByOtp(UUID otp);
}
