package dev.louisa.jam.hub.interfaces.registration;

import dev.louisa.jam.hub.application.registration.RegistrationApplicationService;
import dev.louisa.jam.hub.domain.registration.UserRegistrationId;
import dev.louisa.jam.hub.domain.registration.VerifyRegistrationRequest;
import dev.louisa.jam.hub.interfaces.common.IdResponse;
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

    private final RegistrationApplicationService registrationApplicationService;

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<IdResponse> register(@RequestBody RegistrationRequest request) {
        final UserRegistrationId userRegistrationId = registrationApplicationService.register(request.toDomain());
        return ResponseEntity.status(CREATED).body(IdResponse.from(userRegistrationId));
    }

    @PostMapping("/{registrationId}/verify")
    @ResponseStatus(NO_CONTENT)
    public void verify(@PathVariable UUID registrationId, @RequestBody VerifyRegistrationRequest request) {
        registrationApplicationService.verifyOtp(UserRegistrationId.fromUUID(registrationId), request.otp(), request.password());
    }
}