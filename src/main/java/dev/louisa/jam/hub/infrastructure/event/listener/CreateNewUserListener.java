package dev.louisa.jam.hub.infrastructure.event.listener;

import dev.louisa.jam.hub.domain.registration.event.UserVerifiedEvent;
import dev.louisa.jam.hub.infrastructure.aop.AsyncEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateNewUserListener implements DomainEventListener<UserVerifiedEvent> {

    @AsyncEventListener
    public void on(UserVerifiedEvent event) {
        log.info("Creating new user for  email '{}'", event.emailAddress());
        
    }
}