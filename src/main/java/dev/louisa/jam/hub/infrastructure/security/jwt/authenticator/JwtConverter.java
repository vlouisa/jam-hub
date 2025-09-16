package dev.louisa.jam.hub.infrastructure.security.jwt.authenticator;

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
    
    /**
     * Converts a Decoded JWT token into a UserPrincipal. UserPrincipal is used by Spring Security to allow
     * or deny access to a secured resource.
     *
     * @param decodedJWT, the decoded JWT token
     * @return UserPrincipal
     */
    public UserPrincipal convert(DecodedJWT decodedJWT) {
        return UserPrincipal.builder()
                .userId(UUID.fromString(decodedJWT.getSubject()))
                .authorities(Collections.emptyList())
                .build();
    }
}

