package dev.louisa.jam.hub.domain.user;

import dev.louisa.jam.hub.domain.shared.Id;
import dev.louisa.jam.hub.domain.user.exceptions.UserDomainException;
import lombok.*;

import java.util.Optional;
import java.util.UUID;

import static dev.louisa.jam.hub.domain.user.exceptions.UserDomainError.USER_ID_CANNOT_BE_EMPTY;


@Builder
public record UserId(UUID id) implements Id {

    public UserId {
        id = Optional.of(id)
                .orElseThrow(() -> new UserDomainException(USER_ID_CANNOT_BE_EMPTY));
    }

    public static UserId generate() {
        return UserId.fromUUID(UUID.randomUUID());
    }

    public static UserId fromUUID(UUID uuid) {
        return UserId.builder()
                .id(uuid)
                .build();
    }

    public static UserId fromString(String value) {
        return UserId.fromUUID(UUID.fromString(value));
    }
}
