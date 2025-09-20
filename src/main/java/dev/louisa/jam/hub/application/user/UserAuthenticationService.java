package dev.louisa.jam.hub.application.user;

import dev.louisa.jam.hub.application.exceptions.ApplicationException;
import dev.louisa.jam.hub.application.user.port.inbound.AuthenticateUser;
import dev.louisa.jam.hub.application.user.port.outbound.JwtProvider;
import dev.louisa.jam.hub.application.user.port.outbound.PasswordHasher;
import dev.louisa.jam.hub.application.user.port.outbound.UserRepository;
import dev.louisa.jam.hub.domain.user.Authentication;
import dev.louisa.jam.hub.domain.user.Credentials;
import dev.louisa.jam.hub.infrastructure.security.exception.SecurityError;
import dev.louisa.jam.hub.infrastructure.security.exception.SecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static dev.louisa.jam.hub.application.exceptions.ApplicationError.*;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService implements AuthenticateUser {
    private final PasswordHasher passwordHasher;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    
    @Transactional
    @Override
    public Authentication authenticate(Credentials credentials) {
        var user = userRepository.findByEmail(credentials.emailAddress())
                .orElseThrow(() -> new ApplicationException(ENTITY_NOT_FOUND));
        
        if (!credentials.password().matches(user.getHashedPassword(), passwordHasher)) {
            throw new SecurityException(SecurityError.INVALID_CREDENTIALS);
        }
        
        return Authentication.builder()
                .userId(user.getId())
                .accessToken(jwtProvider.generate(user.getId()))
                .build();
    }
}
