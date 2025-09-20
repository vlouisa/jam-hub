package dev.louisa.jam.hub.api.auth;

import dev.louisa.jam.hub.infrastructure.security.jwt.provider.JwtProviderImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtProviderImpl jwtProvider;

    @GetMapping("/{userId}")
    public ResponseEntity<AuthenticationResponse> login(@PathVariable UUID userId) {

        String jwt = jwtProvider.generate(userId);
        return ResponseEntity.ok(AuthenticationResponse.builder()
                .jwt(jwt)
                .build());
    }


}