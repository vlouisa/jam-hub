package dev.louisa.jam.hub.infrastructure.mail.exception;

import dev.louisa.jam.hub.exceptions.JamHubError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum MailError implements JamHubError {
    SMTP_ERROR( "000", "SMTP generic error", INTERNAL_SERVER_ERROR),
    SMTP_AUTHENTICATION_ERROR( "001", "SMTP authentication error", INTERNAL_SERVER_ERROR),
    SMTP_RECIPIENT_ERROR( "002", "SMTP recipient error", INTERNAL_SERVER_ERROR),
    SMTP_SENDER_ERROR( "003", "SMTP sender error", INTERNAL_SERVER_ERROR);

    private static final String DOMAIN_CODE = "MAI";
    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;

    @Override
    public String getDomainCode() {
        return DOMAIN_CODE;
    }
}
