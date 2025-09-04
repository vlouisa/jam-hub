package dev.louisa.jam.hub.domain.registration;

import dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainException;
import dev.louisa.jam.hub.domain.shared.Id;
import lombok.Builder;

import java.util.Optional;
import java.util.UUID;

import static dev.louisa.jam.hub.domain.user.exceptions.UserDomainError.USER_ID_CANNOT_BE_EMPTY;


@Builder
public record UserRegistrationId(UUID id) implements Id {

    public UserRegistrationId {
        id = Optional.of(id)
                .orElseThrow(() -> new UserRegistrationDomainException(USER_ID_CANNOT_BE_EMPTY));
    }

    public static UserRegistrationId generate() {
        return UserRegistrationId.fromUUID(UUID.randomUUID());
    }

    public static UserRegistrationId fromUUID(UUID uuid) {
        return UserRegistrationId.builder()
                .id(uuid)
                .build();
    }

    public static UserRegistrationId fromString(String value) {
        return UserRegistrationId.fromUUID(UUID.fromString(value));
    }
}
