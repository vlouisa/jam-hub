package dev.louisa.jam.hub.domain.user;

import dev.louisa.jam.hub.domain.common.Guard;
import dev.louisa.jam.hub.domain.common.Id;
import dev.louisa.jam.hub.domain.user.exceptions.UserDomainException;
import lombok.*;

import java.util.UUID;

import static dev.louisa.jam.hub.domain.user.exceptions.UserDomainError.USER_ID_CANNOT_BE_EMPTY;


@Builder
public record UserId(UUID id) implements Id {
    private static final UserDomainException EMPTY_ID_EXCEPTION = new UserDomainException(USER_ID_CANNOT_BE_EMPTY);

    public UserId {
        Guard.when(id == null)
                .thenThrow(EMPTY_ID_EXCEPTION);
    }

    public static UserId generate() {
        return UserId.fromUUID(UUID.randomUUID());
    }

    public static UserId fromUUID(UUID uuid) {
        Guard.when(uuid == null)
                .thenThrow(EMPTY_ID_EXCEPTION);
        
        return UserId.builder()
                .id(uuid)
                .build();
    }

    public static UserId fromString(String value) {
        Guard.when(value == null || value.isBlank())
                .thenThrow(EMPTY_ID_EXCEPTION);
        
        return UserId.fromUUID(UUID.fromString(value));
    }
}
