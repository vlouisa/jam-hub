package dev.louisa.jam.hub.infrastructure.security.jwt;

import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.MissingClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import dev.louisa.jam.hub.infrastructure.security.UserPrincipalAuthenticationToken;
import dev.louisa.jam.hub.infrastructure.security.exception.SecurityException;
import dev.louisa.jam.hub.infrastructure.security.util.BearerTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtConverter jwtConverter;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        authenticate(request);
        filterChain.doFilter(request, response);
    }

    private void authenticate(HttpServletRequest request) {
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
        try {
            extractAuthorizationTokenFrom(request)
                    .map(jwtConverter::convert)
                    .map(UserPrincipalAuthenticationToken::new)
                    .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));
        } catch (SecurityException ex) {
            log.error("Error occurred: [{}-{}, {}, HttpStatus={}]",
                    ex.getError().getDomainCode(),
                    ex.getError().getErrorCode(),
                    ex.getError().getMessage(),
                    ex.getHttpStatus());
        } catch (TokenExpiredException | IncorrectClaimException | MissingClaimException |
                 SignatureVerificationException ex) {
            log.error(
                    "Error occurred: [{}-{}, {}, HttpStatus={}]",
                    JWT_VERIFICATION_ERROR.getDomainCode(),
                    JWT_VERIFICATION_ERROR.getErrorCode(),
                    ex.getMessage(),
                    JWT_VERIFICATION_ERROR.getHttpStatus(),
                    ex);
        }
    }

    private Optional<String> extractAuthorizationTokenFrom(HttpServletRequest request) {
        return Optional.of(
                BearerTokenUtil.extractTokenFrom(request)
                        .orElseThrow(() -> new SecurityException(NO_BEARER_TOKEN)));
    }
}

