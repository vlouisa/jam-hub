package dev.louisa.jam.hub.domain.gig.exceptions;

import dev.louisa.jam.hub.exceptions.JamHubError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Getter
public enum GigDomainError implements JamHubError {
    GIG_ID_CANNOT_BE_EMPTY( "001", "GigId cannot be empty", BAD_REQUEST),
    GIG_ID_MUST_BE_A_VALID_UUID( "002", "GigId must be a valid UUID", BAD_REQUEST),
    GIG_CANNOT_BE_PROMOTED( "020", "Only 'options' can be promoted", BAD_REQUEST);
    
    
    private static final String DOMAIN_CODE = "GIG";
    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
    
    @Override
    public String getDomainCode() {
        return DOMAIN_CODE;
    }
}
