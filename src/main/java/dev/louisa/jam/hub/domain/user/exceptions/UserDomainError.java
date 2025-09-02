package dev.louisa.jam.hub.domain.user.exceptions;

import dev.louisa.jam.hub.exceptions.JamHubError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Getter
public enum UserDomainError implements JamHubError {
    USER_ID_CANNOT_BE_EMPTY( "001", "UserId cannot be empty", BAD_REQUEST),
    USER_ID_MUST_BE_A_VALID_UUID( "002", "UserId must be a valid UUID", BAD_REQUEST);
    
    
    private static final String DOMAIN_CODE = "USR";
    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
    
    @Override
    public String getDomainCode() {
        return DOMAIN_CODE;
    }
}
