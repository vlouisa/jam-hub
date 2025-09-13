package dev.louisa.jam.hub.infrastructure.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final EmailSession emailSession;

    public void sendEmail(Email email) {
        final Session session = emailSession.create();
        try {
            final MimeMessage message = createMessage(email, session);
            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Error sending email: {} ", e.getMessage(), e);
        }
    }

    private MimeMessage createMessage(Email email, Session session) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = new MimeMessage(session);
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(email.from().address(), email.from().personal());
        helper.setSubject(email.subject());
        helper.setTo(email.to().address());
        helper.setText(email.body(), true);
        return message;
    }
}
