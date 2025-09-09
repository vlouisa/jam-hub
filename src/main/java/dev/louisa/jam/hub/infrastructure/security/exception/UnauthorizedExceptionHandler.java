package dev.louisa.jam.hub.infrastructure.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.louisa.jam.hub.infrastructure.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.*;

@Component
@RequiredArgsConstructor
public class UnauthorizedExceptionHandler implements AuthenticationEntryPoint {
    private final ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(
                mapper.writeValueAsString(
                        ErrorResponse.builder()
                                .errorCode(AUTHORIZATION_ERROR.getDomainCode() + "-" + AUTHENTICATION_ERROR.getErrorCode())
                                .message(AUTHENTICATION_ERROR.getMessage())
                                .context(List.of())
                                .build()));
    }
}
