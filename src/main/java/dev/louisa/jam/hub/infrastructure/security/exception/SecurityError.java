package dev.louisa.jam.hub.infrastructure.security.exception;

import dev.louisa.jam.hub.exceptions.JamHubError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RequiredArgsConstructor
@Getter
public enum SecurityError implements JamHubError {
    NO_BEARER_TOKEN( "001", "No bearer token", FORBIDDEN),
    JWT_VERIFICATION_ERROR( "010", "JWT Verification error", FORBIDDEN)
    ;
    
    private static final String DOMAIN_CODE = "BND";
    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
    
    @Override
    public String getDomainCode() {
        return DOMAIN_CODE;
    }
}
