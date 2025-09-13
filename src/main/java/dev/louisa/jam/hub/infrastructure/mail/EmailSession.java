package dev.louisa.jam.hub.infrastructure.mail;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@RequiredArgsConstructor
public class EmailSession {
    private final EmailProperties emailProperties;

    public Session create() {
        final Session session = Session.getInstance(
                createSessionProperties(),
                createSessionAuthenticator());
        session.setDebug(true);

        return session;
    }

    private Properties createSessionProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", emailProperties.getSmtp().getHost());
        props.put("mail.smtp.port", emailProperties.getSmtp().getPort());
        props.put("mail.smtp.auth", emailProperties.getSmtp().getAuth());
        props.put("mail.smtp.starttls.enable", emailProperties.getSmtp().getStarttls().getEnable());
        props.put("mail.smtp.ssl.protocols", emailProperties.getSmtp().getSsl().getProtocol());
        return props;
    }

    private Authenticator createSessionAuthenticator() {
        return new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailProperties.getUser(), emailProperties.getPassword());
            }
        };
    }
}
