package dev.louisa.jam.hub.application.registration;

import dev.louisa.jam.hub.application.common.DomainEventListener;
import dev.louisa.jam.hub.domain.user.event.UserCreatedEvent;
import dev.louisa.jam.hub.infrastructure.aop.AsyncEventListener;
import dev.louisa.jam.hub.infrastructure.mail.Email;
import dev.louisa.jam.hub.infrastructure.mail.EmailAddress;
import dev.louisa.jam.hub.infrastructure.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendPasswordSetupMailListener implements DomainEventListener<UserCreatedEvent> {
    private final EmailService emailService;


    @AsyncEventListener
    public void on(UserCreatedEvent event) {
        log.info("Sending password setup e-mail [user-id={}] to '{}'", event.userId(), event.emailAddress().email());
        emailService.sendEmail(
                Email.builder()
                        .from(
                                EmailAddress.builder()
                                        .address("victor.louisa@jam-hub.test")
                                        .personal("Victor from JAM Hub")
                                        .build())
                        .to(
                                EmailAddress.builder()
                                        .address(event.emailAddress().email())
                                        .personal(event.emailAddress().email())
                                        .build()
                        )
                        .subject("Please set up your password for JAM Hub")
                        .body("""
                                <p>Dear user,</p>
                                <p>Welcome to JAM Hub! Your account has been created successfully.</p>
                                <p>Please click this link to set up your password and complete your registration:</p>
                                <h2><a href="https://jam-hub.test/set-password?userId=%s">Set Up Password</a></h2>
                                <p>If you did not request this, please ignore this email.</p>
                                <p>Best regards,<br/>The JAM Hub Team</p>
                                """.formatted(event.userId()))
                        .build()

        );
    }
}