package dev.louisa.jam.hub.infrastructure.exceptions;

import dev.louisa.jam.hub.exceptions.GenericException;
import dev.louisa.jam.hub.exceptions.JamHubException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static dev.louisa.jam.hub.exceptions.GenericError.*;
import static dev.louisa.jam.hub.infrastructure.exceptions.ExceptionHandlerBuilder.*;
import static dev.louisa.jam.hub.infrastructure.exceptions.InfrastructureError.*;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {JamHubException.class})
    protected ResponseEntity<Object> handleJamHubException(JamHubException ex, WebRequest request) {
        logApplicationError(ex);

        return handle(ex, this::handleExceptionInternal)
                .errorDetails(ex.getError(), ex.getContexts())
                .request(request)
                .toResponseEntity();
    }

    @ExceptionHandler(value = {CannotCreateTransactionException.class})
    protected ResponseEntity<Object> handleDatabaseConnectionException(CannotCreateTransactionException ex, WebRequest request) {
        logApplicationError(new InfrastructureException(DB_CONNECTION_ERROR, ex));

        return handle(ex, this::handleExceptionInternal)
                .errorDetails(DB_CONNECTION_ERROR)
                .request(request)
                .toResponseEntity();
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        logApplicationError(new GenericException(GENERIC_ERROR, ex));

        return handle(ex, this::handleExceptionInternal)
                .errorDetails(GENERIC_ERROR)
                .request(request)
                .toResponseEntity();
    }

    private void logApplicationError(JamHubException exception) {
        log.error("Error occurred: [{}, HttpStatus={}]", exception.getMessage(), exception.getError().getHttpStatus(), exception);
    }
}
