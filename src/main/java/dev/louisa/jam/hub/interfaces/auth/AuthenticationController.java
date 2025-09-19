package dev.louisa.jam.hub.interfaces.auth;

import dev.louisa.jam.hub.infrastructure.security.jwt.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtProvider jwtProvider;

    @GetMapping("/{userId}")
    public ResponseEntity<AuthenticationResponse> login(@PathVariable UUID userId) {

        String jwt = jwtProvider.generate(userId);
        return ResponseEntity.ok(AuthenticationResponse.builder()
                .jwt(jwt)
                .build());
    }


}