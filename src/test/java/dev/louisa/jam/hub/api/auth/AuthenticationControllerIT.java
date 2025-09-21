package dev.louisa.jam.hub.api.auth;

import dev.louisa.jam.hub.application.auth.port.outbound.PasswordHasher;
import dev.louisa.jam.hub.application.auth.port.outbound.UserRepository;
import dev.louisa.jam.hub.infrastructure.ErrorResponse;
import dev.louisa.jam.hub.testsupport.base.BaseApiIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static dev.louisa.jam.hub.testsupport.Factory.domain.aUser;
import static dev.louisa.jam.hub.testsupport.asserts.ErrorResponseAssert.*;
import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationControllerIT extends BaseApiIT {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordHasher passwordHasher;
    
    
    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldAuthenticateUser() throws Exception {
        var user = aUser
                .usingPasswordHasher(passwordHasher)
                .usingRepository(userRepository)
                .createWithCredentials(
                        "reginald.blackbeard@monkey-island.test",
                        "L3tm31n!");
        
        var response = api.post("/api/v1/auth/login")
                .body( loginRequest(
                        "reginald.blackbeard@monkey-island.test",
                        "L3tm31n!"))
                .expectResponseStatus(HttpStatus.OK)
                .send()
                .andReturn(LoginResponse.class);
    
        assertThat(response.userId()).isEqualTo(user.getId().id());
        assertThat(response.accessToken()).isNotNull();
    }

    @Test
    void shouldInformWhenUserProvidesIncorrectPassword() throws Exception {
        aUser
                .usingPasswordHasher(passwordHasher)
                .usingRepository(userRepository)
                .createWithCredentials(
                        "reginald.blackbeard@monkey-island.test",
                        "L3tm31n!");
        
        var response = api.post("/api/v1/auth/login")
                .body( loginRequest(
                        "reginald.blackbeard@monkey-island.test",
                        "G0ld3nR0d!"))
                .expectResponseStatus(HttpStatus.UNAUTHORIZED)
                .send()
                .andReturn(ErrorResponse.class);
    
        assertThatErrorResponse(response)
                .hasErrorCode("SEC-020")
                .hasMessage("Invalid credentials");
    }

    @Test
    void shouldInformWhenUserDoesNotExist() throws Exception {
        var response = api.post("/api/v1/auth/login")
                .body( loginRequest(
                        "voodoo.lady@monkey-island.test",
                        "G0ld3nR0d!"))
                .expectResponseStatus(HttpStatus.UNAUTHORIZED)
                .send()
                .andReturn(ErrorResponse.class);
    
        assertThatErrorResponse(response)
                .hasErrorCode("SEC-020")
                .hasMessage("Invalid credentials");
    }

    private LoginRequest loginRequest(String username, String password) {
        return LoginRequest.builder()
                .username(username)
                .password(password)
                .build();
    }
}
