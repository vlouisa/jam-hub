package dev.louisa.jam.hub.domain.gig;

import dev.louisa.jam.hub.domain.gig.exceptions.GigDomainException;
import dev.louisa.jam.hub.domain.shared.Guard;
import dev.louisa.jam.hub.domain.shared.Id;
import lombok.*;

import java.util.UUID;

import static dev.louisa.jam.hub.domain.gig.exceptions.GigDomainError.GIG_ID_CANNOT_BE_EMPTY;

@Builder
public record GigId(UUID id) implements Id {
    private static final GigDomainException EMPTY_ID_EXCEPTION = new GigDomainException(GIG_ID_CANNOT_BE_EMPTY);

    public GigId {
        Guard.when(id == null)
                .thenThrow(EMPTY_ID_EXCEPTION);
    }

    public static GigId generate() {
        return GigId.fromUUID(UUID.randomUUID());
    }

    public static GigId fromUUID(UUID uuid) {
        Guard.when(uuid == null)
                .thenThrow(EMPTY_ID_EXCEPTION);

        return GigId.builder()
                .id(uuid)
                .build();
    }

    public static GigId fromString(String value) {
        Guard.when(value == null || value.isBlank())
                .thenThrow(EMPTY_ID_EXCEPTION);

        return GigId.fromUUID(UUID.fromString(value));
    }
}
