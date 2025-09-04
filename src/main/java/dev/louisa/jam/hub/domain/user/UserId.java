package dev.louisa.jam.hub.domain.user;

import dev.louisa.jam.hub.domain.shared.Id;
import dev.louisa.jam.hub.domain.user.exceptions.UserDomainException;
import lombok.*;

import java.util.UUID;

import static dev.louisa.jam.hub.domain.shared.Validator.validate;
import static dev.louisa.jam.hub.domain.user.exceptions.UserDomainError.USER_ID_CANNOT_BE_EMPTY;


@Builder
public record UserId(UUID id) implements Id {

    public UserId {
        validate(id)
                .ifNullThrow(new UserDomainException(USER_ID_CANNOT_BE_EMPTY));
    }

    public static UserId generate() {
        return UserId.fromUUID(UUID.randomUUID());
    }

    public static UserId fromUUID(UUID uuid) {
        validate(uuid)
                .ifNullThrow(new UserDomainException(USER_ID_CANNOT_BE_EMPTY));

        return UserId.builder()
                .id(uuid)
                .build();
    }

    public static UserId fromString(String value) {
        validate(value)
                .ifNullOrEmptyThrow(new UserDomainException(USER_ID_CANNOT_BE_EMPTY));
        
        return UserId.fromUUID(UUID.fromString(value));
    }
}
