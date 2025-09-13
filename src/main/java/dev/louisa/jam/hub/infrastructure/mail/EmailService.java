package dev.louisa.jam.hub.infrastructure.mail;

import dev.louisa.jam.hub.infrastructure.mail.exception.MailException;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

import static dev.louisa.jam.hub.infrastructure.mail.exception.MailError.*;

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
        } catch (AuthenticationFailedException e) {
            throw new MailException(SMTP_AUTHENTICATION_ERROR, e);
        } catch (MessagingException e) {
            throw new MailException(SMTP_SENDER_ERROR, e);
        } catch (UnsupportedEncodingException e) {
            throw new MailException(SMTP_ERROR, e);
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
