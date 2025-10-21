package dev.louisa.jam.hub.infrastructure.exceptions;

import dev.louisa.jam.hub.exceptions.JamHubError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum InfrastructureError implements JamHubError {
    DB_CONNECTION_ERROR( "001", "Database connection error", SERVICE_UNAVAILABLE),
    CIRCUIT_BREAKER_OPEN( "100", "Circuit breaker is OPEN, no requests allowed", SERVICE_UNAVAILABLE);
    
    private static final String DOMAIN_CODE = "INF";
    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
    
    @Override
    public String getDomainCode() {
        return DOMAIN_CODE;
    }
}
