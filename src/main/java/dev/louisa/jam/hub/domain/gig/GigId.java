package dev.louisa.jam.hub.domain.gig;

import dev.louisa.jam.hub.domain.gig.exceptions.GigDomainException;
import dev.louisa.jam.hub.domain.shared.Id;
import lombok.*;

import java.util.UUID;

import static dev.louisa.jam.hub.domain.gig.exceptions.GigDomainError.GIG_ID_CANNOT_BE_EMPTY;
import static dev.louisa.jam.hub.domain.shared.Validator.*;

@Builder
public record GigId(UUID id) implements Id {

    public GigId {
        validate(id)
                .ifNullThrow(new GigDomainException(GIG_ID_CANNOT_BE_EMPTY));
    }

    public static GigId generate() {
        return GigId.fromUUID(UUID.randomUUID());
    }

    public static GigId fromUUID(UUID uuid) {
        validate(uuid)
                .ifNullThrow(new GigDomainException(GIG_ID_CANNOT_BE_EMPTY));

        return GigId.builder()
                .id(uuid)
                .build();
    }

    public static GigId fromString(String value) {
        validate(value)
                .ifNullOrEmptyThrow(new GigDomainException(GIG_ID_CANNOT_BE_EMPTY));
        
        return GigId.fromUUID(UUID.fromString(value));
    }
}
