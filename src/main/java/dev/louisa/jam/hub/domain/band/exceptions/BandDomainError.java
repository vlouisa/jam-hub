package dev.louisa.jam.hub.domain.band.exceptions;

import dev.louisa.jam.hub.exceptions.JamHubError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum BandDomainError implements JamHubError {
    BAND_ID_CANNOT_BE_EMPTY( "001", "BandId cannot be empty", BAD_REQUEST),
    BAND_ID_MUST_BE_A_VALID_UUID( "002", "BandId must be a valid UUID", BAD_REQUEST);
    
    
    private static final String DOMAIN_CODE = "BND";
    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
    
    @Override
    public String getDomainCode() {
        return DOMAIN_CODE;
    }
}
