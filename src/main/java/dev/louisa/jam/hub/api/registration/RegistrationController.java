package dev.louisa.jam.hub.api.registration;

import dev.louisa.jam.hub.domain.registration.UserRegistrationId;
import dev.louisa.jam.hub.domain.registration.VerifyRegistrationRequest;
import dev.louisa.jam.hub.application.registration.port.inbound.RegisterUser;
import dev.louisa.jam.hub.application.registration.port.inbound.VerifyRegistration;
import dev.louisa.jam.hub.api.common.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegisterUser registerUser;
    private final VerifyRegistration verifyRegistration;

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<IdResponse> register(@RequestBody RegistrationRequest request) {
        final UserRegistrationId userRegistrationId = registerUser.register(request.toDomain());
        return ResponseEntity.status(CREATED).body(IdResponse.from(userRegistrationId));
    }

    @PostMapping("/{registrationId}/verify")
    @ResponseStatus(NO_CONTENT)
    public void verify(@PathVariable UUID registrationId, @RequestBody VerifyRegistrationRequest request) {
        verifyRegistration.verify(UserRegistrationId.fromUUID(registrationId), request.otp(), request.password());
    }
}