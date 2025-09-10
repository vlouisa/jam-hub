package dev.louisa.jam.hub.interfaces.registration;

import dev.louisa.jam.hub.domain.shared.EmailAddress;
import lombok.Builder;

@Builder
public record RegistrationRequest(String email) {
    public EmailAddress toDomain() {
        return EmailAddress.builder()
                .email(email)
                .build();
    }
}
