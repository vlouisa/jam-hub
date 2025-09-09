package dev.louisa.jam.hub.infrastructure.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.louisa.jam.hub.infrastructure.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.AUTHORIZATION_ERROR;

@Component
@RequiredArgsConstructor
public class ForbiddenExceptionHandler implements AccessDeniedHandler {
    private final ObjectMapper mapper;
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(
                mapper.writeValueAsString(
                        ErrorResponse.builder()
                                .errorCode(AUTHORIZATION_ERROR.getDomainCode() + "-" + AUTHORIZATION_ERROR.getErrorCode())
                                .message(AUTHORIZATION_ERROR.getMessage())
                                .context(List.of())
                                .build()));
    }
}
