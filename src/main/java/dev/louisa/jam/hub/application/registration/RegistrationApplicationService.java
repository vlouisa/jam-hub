package dev.louisa.jam.hub.application.registration;

import dev.louisa.jam.hub.application.exceptions.ApplicationException;
import dev.louisa.jam.hub.application.user.PasswordFactory;
import dev.louisa.jam.hub.domain.event.DomainEventPublisher;
import dev.louisa.jam.hub.domain.registration.UserRegistration;
import dev.louisa.jam.hub.domain.registration.UserRegistrationId;
import dev.louisa.jam.hub.domain.registration.persistence.UserRegistrationRepository;
import dev.louisa.jam.hub.domain.common.EmailAddress;
import dev.louisa.jam.hub.domain.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static dev.louisa.jam.hub.application.exceptions.ApplicationError.ENTITY_NOT_FOUND;
import static dev.louisa.jam.hub.application.exceptions.ApplicationError.USER_ALREADY_EXIST;
import static dev.louisa.jam.hub.domain.user.User.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationApplicationService {
    private final UserRegistrationRepository userRegistrationRepository;
    private final UserRepository userRepository;
    private final PasswordFactory passwordFactory;
    private final DomainEventPublisher publisher;

    @Transactional
    public UserRegistrationId register(EmailAddress emailAddress) {
        userRepository.findByEmail(emailAddress).ifPresent(user -> {
            throw new ApplicationException(USER_ALREADY_EXIST);
        });
        
        final UserRegistration registration = UserRegistration.createNewRegistration(emailAddress);
        userRegistrationRepository.save(registration);
        registration.pullDomainEvents().forEach(publisher::publish);
        return registration.getId();
    }
    
    @Transactional
    public void verifyOtp(UserRegistrationId userRegistrationId, UUID otp, String rawPassword) {
        UserRegistration registration = userRegistrationRepository
                .findById(userRegistrationId)
                .orElseThrow(() -> new ApplicationException(ENTITY_NOT_FOUND));
        
        registration.verifyWithOtp(otp);

        userRepository.save(createNewUser(registration.getEmail(), passwordFactory.from(rawPassword)));
        userRegistrationRepository.save(registration);
        registration.pullDomainEvents().forEach(publisher::publish);
    }
}
