package dev.louisa.jam.hub.infrastructure.security.jwt.authenticator;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.louisa.jam.hub.infrastructure.security.UserPrincipal;
import dev.louisa.jam.hub.infrastructure.security.UserPrincipalAuthenticationToken;
import dev.louisa.jam.hub.infrastructure.security.exception.SecurityException;
import dev.louisa.jam.hub.testsupport.base.BaseInfraStructureTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationServiceTest extends BaseInfraStructureTest {
    private static final SimpleGrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");
    private static final SimpleGrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
    private static final UUID USER_ID = UUID.randomUUID();
    
    @Mock
    private DecodedJWT decodedJWT;
    @Mock
    private JwtValidator jwtValidator;
    @Mock
    private JwtConverter jwtConverter;

    private JwtAuthenticationService jwtAuthenticationService;

    @BeforeEach
    void setUp() {
        jwtAuthenticationService = new JwtAuthenticationService(jwtValidator, jwtConverter);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateAndSetSecurityContextWhenTokenIsValid() {
        when(jwtValidator.validate("valid-token"))
                .thenReturn(decodedJWT);
        when(jwtConverter.convert(decodedJWT))
                .thenReturn(new UserPrincipal(USER_ID, List.of(ROLE_USER, ROLE_ADMIN)));

        jwtAuthenticationService.authenticate("valid-token");

        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isInstanceOf(UserPrincipalAuthenticationToken.class)
                .extracting(
                        auth -> ((UserPrincipalAuthenticationToken) auth).getPrincipal().getUsername(),
                        auth -> ((UserPrincipalAuthenticationToken) auth).getPrincipal().getAuthorities()
                )
                .containsExactly(USER_ID.toString(), List.of(ROLE_USER, ROLE_ADMIN));

        verify(jwtValidator).validate("valid-token");
        verify(jwtConverter).convert(decodedJWT);
    }

    @Test
    void shouldNotAuthenticateWhenJwtVerificationExceptionThrown() {
        String token = "tampered-token";
        when(jwtValidator.validate(token))
                .thenThrow(new JWTVerificationException("Signature invalid"));

        assertThatCode(() -> jwtAuthenticationService.authenticate(token))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("JWT Verification error");;

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(jwtValidator).validate(token);
        verifyNoInteractions(jwtConverter);
    }
}