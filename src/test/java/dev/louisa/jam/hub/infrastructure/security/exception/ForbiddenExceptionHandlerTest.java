package dev.louisa.jam.hub.infrastructure.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.louisa.jam.hub.infrastructure.ErrorResponse;
import dev.louisa.jam.hub.testsupport.base.BaseInfraStructureTest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;

import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.AUTHORIZATION_ERROR;
import static dev.louisa.jam.hub.testsupport.asserts.ErrorResponseAssert.*;
import static org.assertj.core.api.Assertions.assertThat;

class ForbiddenExceptionHandlerTest extends BaseInfraStructureTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ForbiddenExceptionHandler handler = new ForbiddenExceptionHandler(objectMapper);

    @Test
    void shouldReturnForbiddenErrorResponse() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        handler.handle(request, response, new AccessDeniedException("forbidden"));

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_FORBIDDEN);
        assertThat(response.getContentType()).isEqualTo("application/json;charset=UTF-8");
        assertThat(response.getCharacterEncoding()).isEqualTo("UTF-8");

        final String body = response.getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(body, ErrorResponse.class);

        assertThatErrorResponse(errorResponse)
                .hasMessage(AUTHORIZATION_ERROR.getMessage())
                .hasErrorCode(AUTHORIZATION_ERROR.getDomainCode() + "-" + AUTHORIZATION_ERROR.getErrorCode())
                .hasEmptyContext();
    }
}