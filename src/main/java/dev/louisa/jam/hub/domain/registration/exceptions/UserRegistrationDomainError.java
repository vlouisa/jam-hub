package dev.louisa.jam.hub.domain.registration.exceptions;

import dev.louisa.jam.hub.exceptions.JamHubError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Getter
public enum UserRegistrationDomainError implements JamHubError {
    USER_REGISTRATION_ID_CANNOT_BE_EMPTY( "001", "UserRegistrationId cannot be empty", BAD_REQUEST),
    USER_REGISTRATION_ID_MUST_BE_A_VALID_UUID( "002", "UserRegistrationId must be a valid UUID", BAD_REQUEST),
    OTP_CODE_EXPIRED( "010", "OTP code has expired", BAD_REQUEST),
    OTP_CODE_REVOKED( "011", "OTP code has been revoked", BAD_REQUEST)
    ;
    
    
    private static final String DOMAIN_CODE = "URG";
    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
    
    @Override
    public String getDomainCode() {
        return DOMAIN_CODE;
    }
}
