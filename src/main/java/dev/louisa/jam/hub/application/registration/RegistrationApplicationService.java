package dev.louisa.jam.hub.application.registration;

import dev.louisa.jam.hub.application.exceptions.ApplicationException;
import dev.louisa.jam.hub.domain.registration.UserRegistration;
import dev.louisa.jam.hub.domain.registration.UserRegistrationId;
import dev.louisa.jam.hub.domain.registration.persistence.UserRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static dev.louisa.jam.hub.application.exceptions.ApplicationError.ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RegistrationApplicationService {
    private final UserRegistrationRepository userRegistrationRepository;

    public void verifyOtp(UserRegistrationId registrationId) {
        UserRegistration registration = userRegistrationRepository
                .findById(registrationId)
                .orElseThrow(() -> new ApplicationException(ENTITY_NOT_FOUND));
        
        if (registration.isVerified()) {
            return;
        }
        
        registration.verify();
        userRegistrationRepository.save(registration);
    }
}
