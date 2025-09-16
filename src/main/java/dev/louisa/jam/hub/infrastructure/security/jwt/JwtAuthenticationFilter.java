package dev.louisa.jam.hub.infrastructure.security.jwt;

import dev.louisa.jam.hub.infrastructure.security.SecurityLevelResolver;
import dev.louisa.jam.hub.infrastructure.security.jwt.authenticator.JwtAuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static dev.louisa.jam.hub.infrastructure.security.util.RequestTokenExtractor.bearerTokenFrom;

/* Spring security core exceptions (e.g., AuthenticationException and AccessDeniedException) are thrown by the
 * authentication filters behind the DispatcherServlet and before invoking the controller methods. This means
 * that @ControllerAdvice won’t be able to catch these exceptions and log the error properly.
 *
 * Because the DispatcherServlet throws the exception, the logging (stacktrace etc.) is not correlated to a
 * request-id (Set by the InboundRequestLoggingFilter). In other words, the request-id is not available in the MDC context.
 *
 * To log errors AND correlate them to a request we catch the exceptions, log them in this filter.
 * We don't handle the error ourselves, we let Spring security handle the error and return an unauthorized/forbidden
 * http status!
 */


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtAuthenticationService jwtAuthenticationService;
    private final SecurityLevelResolver securityLevelResolver;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        jwtAuthenticationService.authenticate(bearerTokenFrom(request));
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return securityLevelResolver.isUnsecured(request.getRequestURI(), request.getMethod());
    }
}

