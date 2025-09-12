package dev.louisa.jam.hub.application.registration;

import dev.louisa.jam.hub.application.exceptions.ApplicationException;
import dev.louisa.jam.hub.domain.event.DomainEventPublisher;
import dev.louisa.jam.hub.domain.registration.UserRegistration;
import dev.louisa.jam.hub.domain.registration.UserRegistrationId;
import dev.louisa.jam.hub.domain.registration.persistence.UserRegistrationRepository;
import dev.louisa.jam.hub.domain.common.EmailAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static dev.louisa.jam.hub.application.exceptions.ApplicationError.ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationApplicationService {
    private final UserRegistrationRepository userRegistrationRepository;
    private final DomainEventPublisher publisher;

    @Transactional
    public UserRegistrationId register(EmailAddress emailAddress) {
        final UserRegistration userRegistration = UserRegistration.createNewRegistration(emailAddress);
        return userRegistrationRepository.save(userRegistration).getId();
    }
    
    @Transactional
    public void verifyOtp(UUID otp) {
        UserRegistration registration = userRegistrationRepository
                .findByOtp(otp)
                .orElseThrow(() -> new ApplicationException(ENTITY_NOT_FOUND));
        
        if (registration.isVerified()) {
            return;
        }
        
        registration.verify();
        userRegistrationRepository.save(registration);
        
        registration.pullDomainEvents().forEach(publisher::publish);
    }
}
