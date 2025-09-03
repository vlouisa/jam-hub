package dev.louisa.jam.hub.exceptions;

import org.springframework.http.HttpStatus;

public interface JamHubError {
    String getDomainCode();
    String getErrorCode();
    String getMessage();
    HttpStatus getHttpStatus();
}
