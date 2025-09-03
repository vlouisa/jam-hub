package dev.louisa.jam.hub.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum GenericError implements JamHubError {
    GENERIC_ERROR( "000", "A generic error occurred", INTERNAL_SERVER_ERROR);
    
    private static final String DOMAIN_CODE = "GEN";
    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
    
    @Override
    public String getDomainCode() {
        return DOMAIN_CODE;
    }
}
