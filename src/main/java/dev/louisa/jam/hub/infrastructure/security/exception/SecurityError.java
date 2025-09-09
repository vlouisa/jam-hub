package dev.louisa.jam.hub.infrastructure.security.exception;

import dev.louisa.jam.hub.exceptions.JamHubError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@Getter
public enum SecurityError implements JamHubError {
    AUTHENTICATION_ERROR( "001", "Not authenticated", UNAUTHORIZED),
    AUTHORIZATION_ERROR( "002", "Not authorized", FORBIDDEN),
    NO_BEARER_TOKEN( "010", "No bearer token", UNAUTHORIZED),
    JWT_VERIFICATION_ERROR( "011", "JWT Verification error", UNAUTHORIZED)
    ;
    
    private static final String DOMAIN_CODE = "SEC";
    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
    
    @Override
    public String getDomainCode() {
        return DOMAIN_CODE;
    }
}
