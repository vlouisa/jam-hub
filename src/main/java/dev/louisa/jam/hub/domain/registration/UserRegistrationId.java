package dev.louisa.jam.hub.domain.registration;

import dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainException;
import dev.louisa.jam.hub.domain.shared.Guard;
import dev.louisa.jam.hub.domain.shared.Id;
import lombok.Builder;

import java.util.UUID;

import static dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainError.USER_REGISTRATION_ID_CANNOT_BE_EMPTY;


@Builder
public record UserRegistrationId(UUID id) implements Id {
    private static final UserRegistrationDomainException EMPTY_ID_EXCEPTION = new UserRegistrationDomainException(USER_REGISTRATION_ID_CANNOT_BE_EMPTY);

    public UserRegistrationId {
        Guard.when(id == null)
                .thenThrow(EMPTY_ID_EXCEPTION);
    }

    public static UserRegistrationId generate() {
        return UserRegistrationId.fromUUID(UUID.randomUUID());
    }

    public static UserRegistrationId fromUUID(UUID uuid) {
        Guard.when(uuid == null)
                .thenThrow(EMPTY_ID_EXCEPTION);

        return UserRegistrationId.builder()
                .id(uuid)
                .build();
    }

    public static UserRegistrationId fromString(String value) {
        Guard.when(value == null || value.isBlank())
                .thenThrow(EMPTY_ID_EXCEPTION);

        return UserRegistrationId.fromUUID(UUID.fromString(value));
    }
}
