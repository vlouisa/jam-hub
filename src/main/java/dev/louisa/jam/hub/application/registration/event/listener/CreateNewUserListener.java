package dev.louisa.jam.hub.application.registration.event.listener;

import dev.louisa.jam.hub.domain.registration.event.UserVerifiedEvent;
import dev.louisa.jam.hub.domain.user.User;
import dev.louisa.jam.hub.domain.user.persistence.UserRepository;
import dev.louisa.jam.hub.infrastructure.aop.AsyncEventListener;
import dev.louisa.jam.hub.application.common.DomainEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateNewUserListener implements DomainEventListener<UserVerifiedEvent> {
    private final UserRepository userRepository;
    
    @AsyncEventListener
    public void on(UserVerifiedEvent event) {
        log.info("Creating new user for  email '{}'", event.emailAddress());
        userRepository.save(User.createNewUser(event.emailAddress()));
    }
}