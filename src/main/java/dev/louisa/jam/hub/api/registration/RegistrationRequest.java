package dev.louisa.jam.hub.api.registration;

import dev.louisa.jam.hub.domain.common.EmailAddress;
import lombok.Builder;

@Builder
public record RegistrationRequest(String email) {
    public EmailAddress toDomain() {
        return EmailAddress.builder()
                .email(email)
                .build();
    }
}
