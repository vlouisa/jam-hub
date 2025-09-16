package dev.louisa.jam.hub.infrastructure.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.louisa.jam.hub.infrastructure.security.UserPrincipalAuthenticationToken;
import dev.louisa.jam.hub.infrastructure.security.exception.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.JWT_VERIFICATION_ERROR;
import static dev.louisa.jam.hub.infrastructure.security.util.BearerTokenUtil.tokenFrom;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationService {
    private final JwtConverter jwtConverter;
    
    public void authenticate(HttpServletRequest request) {
        try {
            var userPrincipal = jwtConverter.convert(tokenFrom(request));
            SecurityContextHolder.getContext()
                    .setAuthentication(new UserPrincipalAuthenticationToken(userPrincipal));

        } catch (SecurityException ex) {
            log.error("Error occurred: [{}-{}, {}, HttpStatus={}]",
                    ex.getError().getDomainCode(),
                    ex.getError().getErrorCode(),
                    ex.getError().getMessage(),
                    ex.getHttpStatus());
        } catch (JWTVerificationException ex) {
            log.error(
                    "Error occurred: [{}-{}, {}, HttpStatus={}]",
                    JWT_VERIFICATION_ERROR.getDomainCode(),
                    JWT_VERIFICATION_ERROR.getErrorCode(),
                    ex.getMessage(),
                    JWT_VERIFICATION_ERROR.getHttpStatus(),
                    ex);
        }
    }

}
