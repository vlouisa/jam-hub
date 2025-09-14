package dev.louisa.jam.hub.application.exceptions;

import dev.louisa.jam.hub.exceptions.JamHubError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum ApplicationError implements JamHubError {
    ENTITY_NOT_FOUND( "001", "Entity not found", NOT_FOUND),
    USER_NOT_AUTHORIZED( "100", "User not authorized for this action", FORBIDDEN),
    USER_ALREADY_EXIST( "150", "User not authorized for this action", CONFLICT);
    
    private static final String DOMAIN_CODE = "APP";
    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
    
    @Override
    public String getDomainCode() {
        return DOMAIN_CODE;
    }
}
