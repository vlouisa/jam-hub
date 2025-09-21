package dev.louisa.jam.hub.application.registration;

import dev.louisa.jam.hub.application.exceptions.ApplicationException;
import dev.louisa.jam.hub.application.auth.port.outbound.PasswordHasher;
import dev.louisa.jam.hub.domain.event.DomainEventPublisher;
import dev.louisa.jam.hub.domain.registration.UserRegistration;
import dev.louisa.jam.hub.domain.registration.UserRegistrationId;
import dev.louisa.jam.hub.application.registration.port.outbound.UserRegistrationRepository;
import dev.louisa.jam.hub.domain.common.EmailAddress;
import dev.louisa.jam.hub.application.registration.port.inbound.RegisterUser;
import dev.louisa.jam.hub.application.registration.port.inbound.VerifyRegistration;
import dev.louisa.jam.hub.application.auth.port.outbound.UserRepository;
import dev.louisa.jam.hub.application.auth.Password;
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
public class RegistrationApplicationService implements RegisterUser, VerifyRegistration {
    private final PasswordHasher passwordHasher;
    private final UserRegistrationRepository userRegistrationRepository;
    private final UserRepository userRepository;
    private final DomainEventPublisher publisher;

    @Transactional
    @Override
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
    @Override
    public void verify(UserRegistrationId userRegistrationId, UUID otp, String rawPassword) {
        UserRegistration registration = userRegistrationRepository
                .findById(userRegistrationId)
                .orElseThrow(() -> new ApplicationException(ENTITY_NOT_FOUND));
        
        registration.verifyWithOtp(otp);

        userRepository.save(createNewUser(registration.getEmail(), Password.fromString(rawPassword).hash(passwordHasher)));
        userRegistrationRepository.save(registration);
        registration.pullDomainEvents().forEach(publisher::publish);
    }
}
