package dev.louisa.jam.hub.domain.registration;

import dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainException;
import dev.louisa.jam.hub.domain.shared.Id;
import lombok.Builder;

import java.util.UUID;

import static dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainError.USER_REGISTRATION_ID_CANNOT_BE_EMPTY;
import static dev.louisa.jam.hub.domain.shared.Validator.validate;


@Builder
public record UserRegistrationId(UUID id) implements Id {

    public UserRegistrationId {
        validate(id)
                .ifNullThrow(new UserRegistrationDomainException(USER_REGISTRATION_ID_CANNOT_BE_EMPTY));
    }

    public static UserRegistrationId generate() {
        return UserRegistrationId.fromUUID(UUID.randomUUID());
    }

    public static UserRegistrationId fromUUID(UUID uuid) {
        validate(uuid)
                .ifNullThrow(new UserRegistrationDomainException(USER_REGISTRATION_ID_CANNOT_BE_EMPTY));

        return UserRegistrationId.builder()
                .id(uuid)
                .build();
    }

    public static UserRegistrationId fromString(String value) {
        validate(value)
                .ifNullOrEmptyThrow(new UserRegistrationDomainException(USER_REGISTRATION_ID_CANNOT_BE_EMPTY));

        return UserRegistrationId.fromUUID(UUID.fromString(value));
    }
}
