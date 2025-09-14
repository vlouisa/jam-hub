package dev.louisa.jam.hub.application.registration.event.listener;

import dev.louisa.jam.hub.application.common.DomainEventListener;
import dev.louisa.jam.hub.domain.registration.event.RegistrationVerifiedEvent;
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
public class SendRegistrationWelcomeMailListener implements DomainEventListener<RegistrationVerifiedEvent> {
    private final EmailService emailService;


    @AsyncEventListener
    public void on(RegistrationVerifiedEvent event) {
        log.info("Sending welcome e-mail [registration-id={}] to '{}'", 
                event.userRegistrationId().toValue(),
                event.emailAddress().email());
        
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
                        .subject("Welcome to JAM Hub! Your account has been created")
                        .body("""
                                <p>Dear user,</p>
                                <p>Welcome to JAM Hub! Your email address has been successfully verified.</p>
                                <p>You can now log in and start using our services.</p>
                                <p>If you have any questions or need assistance, feel free to contact our support team</p>.
                                <p>We're excited to have you on board!</p>
                                <p>Best regards,<br/>The JAM Hub Team</p>
                                """)
                        .build()

        );
    }
}