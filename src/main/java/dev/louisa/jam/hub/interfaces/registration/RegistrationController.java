package dev.louisa.jam.hub.interfaces.registration;

import dev.louisa.jam.hub.application.registration.RegistrationApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationApplicationService registrationApplicationService;


    @PatchMapping("/verify/{registrationId}")
    @ResponseStatus(NO_CONTENT)
    public void verify(@PathVariable String registrationId) {
            registrationApplicationService.verifyOtp(registrationId);
    }
}