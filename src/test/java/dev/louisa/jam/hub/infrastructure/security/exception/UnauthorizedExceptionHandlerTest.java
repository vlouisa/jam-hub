package dev.louisa.jam.hub.infrastructure.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.louisa.jam.hub.infrastructure.ErrorResponse;
import dev.louisa.jam.hub.testsupport.base.BaseInfraStructureTest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.AUTHENTICATION_ERROR;
import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.AUTHORIZATION_ERROR;
import static dev.louisa.jam.hub.testsupport.asserts.ErrorResponseAssert.assertThatErrorResponse;
import static org.assertj.core.api.Assertions.assertThat;

class UnauthorizedExceptionHandlerTest extends BaseInfraStructureTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UnauthorizedExceptionHandler handler = new UnauthorizedExceptionHandler(objectMapper);

    @Test
    void shouldReturnUnauthorizedErrorResponse() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        handler.commence(request, response, new org.springframework.security.core.AuthenticationException("unauthorized") {});

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(response.getContentType()).isEqualTo("application/json;charset=UTF-8");
        assertThat(response.getCharacterEncoding()).isEqualTo("UTF-8");

        String body = response.getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(body, ErrorResponse.class);


        assertThatErrorResponse(errorResponse)
                .hasMessage(AUTHENTICATION_ERROR.getMessage())
                .hasErrorCode(AUTHORIZATION_ERROR.getDomainCode() + "-" + AUTHENTICATION_ERROR.getErrorCode())
                .hasEmptyContext(); 
    }
}