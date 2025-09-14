package dev.louisa.jam.hub.infrastructure.exceptions;

import dev.louisa.jam.hub.domain.user.UserId;
import dev.louisa.jam.hub.domain.user.exceptions.UserDomainException;
import dev.louisa.jam.hub.testsupport.base.BaseInfraStructureTest;
import dev.louisa.jam.hub.infrastructure.ErrorResponse;
import dev.louisa.jam.hub.testsupport.LoggerSpy;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static ch.qos.logback.classic.Level.*;
import static dev.louisa.jam.hub.domain.user.exceptions.UserDomainError.*;
import static dev.louisa.jam.hub.testsupport.asserts.ErrorResponseAssert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

class RestResponseEntityExceptionHandlerTest extends BaseInfraStructureTest {
    private static final WebRequest REQUEST = null;
    private static final UserId VALID_USER_ID = UserId.fromString("123e4567-e89b-12d3-a456-426614174000");// can be mocked if needed

    private final LoggerSpy loggerSpy = LoggerSpy.forClass(RestResponseEntityExceptionHandler.class);
    
    private final RestResponseEntityExceptionHandler handler = new RestResponseEntityExceptionHandler();

    @Test
    void shouldHandleJamHubException() {
        var ex = new UserDomainException(USER_ID_MUST_BE_A_VALID_UUID, List.of(VALID_USER_ID));
        ResponseEntity<Object> response = handler.handleJamHubException(ex, REQUEST);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isInstanceOf(ErrorResponse.class);

        final ErrorResponse responseBody = (ErrorResponse) response.getBody();
       
        assertThatErrorResponse(responseBody)
                .hasErrorCode("USR-002")
                .hasMessage("UserId must be a valid UUID")
                .hasContext(List.of(VALID_USER_ID.toString()));
        
        loggerSpy
                .assertThatAtLeastOneMessageWithLevel(ERROR)
                .contains("USR-002", "400 BAD_REQUEST", "UserId must be a valid UUID", VALID_USER_ID.toString());
    }

    @Test
    void shouldHandleGenericException() {
        var fail = new Exception("fail");
        ResponseEntity<Object> response = handler.handleGenericException(fail, REQUEST);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isInstanceOf(ErrorResponse.class);

        final ErrorResponse responseBody = (ErrorResponse) response.getBody();

        assertThatErrorResponse(responseBody)
                .hasErrorCode("GEN-000")
                .hasMessage("A generic error occurred")
                .hasEmptyContext();

        loggerSpy
                .assertThatAtLeastOneMessageWithLevel(ERROR)
                .contains("500 INTERNAL_SERVER_ERROR", "A generic error occurred");
    }
}
