package dev.louisa.jam.hub.application.auth;

import dev.louisa.jam.hub.application.auth.port.inbound.AuthenticateUser;
import dev.louisa.jam.hub.application.auth.port.outbound.JwtProvider;
import dev.louisa.jam.hub.application.auth.port.outbound.PasswordHasher;
import dev.louisa.jam.hub.application.auth.port.outbound.UserRepository;
import dev.louisa.jam.hub.infrastructure.security.exception.SecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.INVALID_CREDENTIALS;

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
                .orElseThrow(() -> new SecurityException(INVALID_CREDENTIALS));
        
        if (!credentials.password().matches(user.getHashedPassword(), passwordHasher)) {
            throw new SecurityException(INVALID_CREDENTIALS);
        }
        
        return Authentication.builder()
                .userId(user.getId())
                .accessToken(jwtProvider.generate(user.getId()))
                .build();
    }
}
