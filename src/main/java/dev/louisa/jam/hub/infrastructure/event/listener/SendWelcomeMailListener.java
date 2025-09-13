package dev.louisa.jam.hub.infrastructure.event.listener;

import dev.louisa.jam.hub.domain.registration.event.RegistrationCreatedEvent;
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
public class SendWelcomeMailListener implements DomainEventListener<RegistrationCreatedEvent> {
    private final EmailService emailService;


    @AsyncEventListener
    public void on(RegistrationCreatedEvent event) {
        log.info("Sending verification e-mail [otp={}] to '{}'", event.otp(), event.emailAddress().email());
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
                        .subject("Welcome to JAM Hub! Please verify your email address")
                        .body("""
                                <p>Dear user,</p>
                                <p>Thank you for registering at JAM Hub!</p>
                                <p>Please use the following One-Time Password (OTP) to verify your email address:</p>
                                <h2>%s</h2>
                                <p>This OTP is valid for 15 minutes. If you did not request this, please ignore this email.</p>
                                <p>Best regards,<br/>The JAM Hub Team</p>
                                """.formatted(event.otp()))
                        .build()

        );
    }
}