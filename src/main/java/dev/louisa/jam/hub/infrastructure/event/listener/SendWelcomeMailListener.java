package dev.louisa.jam.hub.infrastructure.event.listener;

import dev.louisa.jam.hub.domain.registration.event.RegistrationCreatedEvent;
import dev.louisa.jam.hub.infrastructure.aop.AsyncEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SendWelcomeMailListener implements DomainEventListener<RegistrationCreatedEvent> {

    @AsyncEventListener
    public void on(RegistrationCreatedEvent event) {
        log.info("Sending verification e-mail [otp={}] to '{}'", event.otp(), event.emailAddress().email());
    }
}