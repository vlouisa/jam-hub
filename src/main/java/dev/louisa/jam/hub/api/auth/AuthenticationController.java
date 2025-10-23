package dev.louisa.jam.hub.api.auth;

import dev.louisa.jam.hub.application.auth.port.inbound.AuthenticateUser;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticateUser authenticateUser;

    @PostMapping("/login")
    @CircuitBreaker(name="database-cb")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        var authentication = authenticateUser.authenticate(request.toCredentials());
        return ResponseEntity.ok(
                LoginResponse.builder()
                        .accessToken(authentication.accessToken())
                        .userId(authentication.userId().id())
                        .build()
        );
    }
}