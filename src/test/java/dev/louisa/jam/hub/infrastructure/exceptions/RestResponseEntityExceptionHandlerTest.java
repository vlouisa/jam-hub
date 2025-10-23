package dev.louisa.jam.hub.infrastructure.exceptions;

import dev.louisa.jam.hub.domain.user.UserId;
import dev.louisa.jam.hub.domain.user.exceptions.UserDomainException;
import dev.louisa.jam.hub.testsupport.base.BaseInfraStructureTest;
import dev.louisa.jam.hub.infrastructure.ErrorResponse;
import dev.louisa.jam.hub.testsupport.LoggerSpy;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLTransientConnectionException;
import java.time.Duration;
import java.util.List;

import static ch.qos.logback.classic.Level.*;
import static dev.louisa.jam.hub.domain.user.exceptions.UserDomainError.*;
import static dev.louisa.jam.hub.testsupport.asserts.ErrorResponseAssert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class RestResponseEntityExceptionHandlerTest extends BaseInfraStructureTest {
    private static final UserId VALID_USER_ID = UserId.fromString("123e4567-e89b-12d3-a456-426614174000");// can be mocked if needed

    private final LoggerSpy loggerSpy = LoggerSpy.forClass(RestResponseEntityExceptionHandler.class);
    private final RestResponseEntityExceptionHandler handler = new RestResponseEntityExceptionHandler();
    
    @Mock
    private WebRequest webRequest;

    @Test
    void shouldHandleJamHubException() {
        var ex = new UserDomainException(USER_ID_MUST_BE_A_VALID_UUID, List.of(VALID_USER_ID));
        ResponseEntity<Object> response = handler.handleJamHubException(ex, webRequest);

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
                .contains(
                        "USR-002", 
                        "400 BAD_REQUEST", 
                        "UserId must be a valid UUID", 
                        VALID_USER_ID.toString());
    }

    @Test
    void shouldHandleDatabaseConnectionException() {
        var fail = new CannotCreateTransactionException("Database connection failed", new JDBCConnectionException("Unable to acquire JDBC Connection", new SQLTransientConnectionException("HikariPool-1 - Connection is not available")));
        ResponseEntity<Object> response = handler.handleDatabaseConnectionException(fail, webRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(SERVICE_UNAVAILABLE);
        assertThat(response.getBody()).isInstanceOf(ErrorResponse.class);

        final ErrorResponse responseBody = (ErrorResponse) response.getBody();

        assertThatErrorResponse(responseBody)
                .hasErrorCode("INF-001")
                .hasMessage("Database connection error")
                .hasEmptyContext();

        loggerSpy
                .assertThatAtLeastOneMessageWithLevel(ERROR)
                .contains(
                        "INF-001",
                        "503 SERVICE_UNAVAILABLE", 
                        "Database connection error");
    }

    @Test
    void shouldHandleCircuitBreakerException() {
        var fail = CallNotPermittedException.createCallNotPermittedException(createCircuitBreaker());
        ResponseEntity<Object> response = handler.handleCallNotPermittedException(fail, webRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(SERVICE_UNAVAILABLE);
        assertThat(response.getBody()).isInstanceOf(ErrorResponse.class);

        final ErrorResponse responseBody = (ErrorResponse) response.getBody();

        assertThatErrorResponse(responseBody)
                .hasErrorCode("INF-100")
                .hasMessage("Circuit breaker is OPEN, no requests allowed")
                .hasEmptyContext();

        loggerSpy
                .assertThatAtLeastOneMessageWithLevel(ERROR)
                .contains(
                        "INF-100",
                        "503 SERVICE_UNAVAILABLE", 
                        "Circuit breaker is OPEN, no requests allowed");
    }

    @Test
    void shouldHandleGenericException() {
        var fail = new Exception("fail");
        ResponseEntity<Object> response = handler.handleGenericException(fail, webRequest);

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
                .contains(
                        "GEN-000",
                        "500 INTERNAL_SERVER_ERROR", 
                        "A generic error occurred");
    }
    
    private CircuitBreaker createCircuitBreaker() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(500))
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .permittedNumberOfCallsInHalfOpenState(2)
                .build();

        return CircuitBreaker.of("testBreaker", config);
    }
}
