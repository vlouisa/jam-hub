package dev.louisa.jam.hub.infrastructure.exceptions;

import dev.louisa.jam.hub.domain.shared.Id;
import dev.louisa.jam.hub.exceptions.JamHubError;
import dev.louisa.jam.hub.exceptions.JamHubException;
import dev.louisa.jam.hub.infrastructure.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static dev.louisa.jam.hub.exceptions.GenericError.*;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {JamHubException.class})
    protected ResponseEntity<Object> handleJamHubException(JamHubException ex, WebRequest request) {
        logApplicationError(
                ex.getMessage(),
                ex.getError().getHttpStatus(),
                ex);


        return handleExceptionInternal(
                ex,
                constructErrorCode(ex.getError(), ex.getContexts()),
                new HttpHeaders(),
                ex.getError().getHttpStatus(),
                request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        logApplicationError(
                GENERIC_ERROR.getMessage(),
                GENERIC_ERROR.getHttpStatus(),
                ex);


        return handleExceptionInternal(
                ex,
                constructErrorCode(GENERIC_ERROR, List.of()),
                new HttpHeaders(),
                GENERIC_ERROR.getHttpStatus(),
                request);
    }

    private ErrorResponse constructErrorCode(JamHubError error, List<Id> context) {
        return ErrorResponse.builder()
                .errorCode(error.getDomainCode() + "-" + error.getErrorCode())
                .message(error.getMessage())
                .context(context.toString().isEmpty() ? List.of() : context.stream().map(Id::toString).toList())
                .build();
    }

    private void logApplicationError(String fullMessage, HttpStatus httpStatus, Exception exception) {
        log.error("Error occurred: [{}, HttpStatus={}]", fullMessage, httpStatus, exception);
    }
}
