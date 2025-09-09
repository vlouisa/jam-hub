package dev.louisa.jam.hub.infrastructure.security.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import dev.louisa.jam.hub.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtConverter {
    private final JwtValidator jwtValidator;
    
    /**
     * Converts a Bearer JWT token (String) into a UserPrincipal. UserPrincipal is used by Spring Security to allow
     * or deny access to a secured resource. The converter requests the proper validator. Once received it uses that
     * validator to validate the token and create a UserPrincipal.
     *
     * @param token String representation of the JWT Bearer token
     * @return UserPrincipal
     */
    public UserPrincipal convert(String token) {
        final DecodedJWT jwt = jwtValidator.validate(token);
        return UserPrincipal.builder()
                .userId(UUID.fromString(jwt.getSubject()))
                .authorities(Collections.emptyList())
                .build();
    }
}

