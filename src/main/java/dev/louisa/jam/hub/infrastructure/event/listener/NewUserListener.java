package dev.louisa.jam.hub.infrastructure.event.listener;

import dev.louisa.jam.hub.domain.registration.event.UserVerifiedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewUserListener {
    
    @Async
    @EventListener
    public void on(UserVerifiedEvent event) {
        log.warn("New user verified: " + event.emailAddress().email());
    }
}