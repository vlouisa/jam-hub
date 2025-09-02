package dev.louisa.jam.hub.domain.gig;

import dev.louisa.jam.hub.domain.band.exceptions.BandDomainException;
import dev.louisa.jam.hub.domain.shared.Id;
import lombok.*;

import java.util.Optional;
import java.util.UUID;

import static dev.louisa.jam.hub.domain.gig.exceptions.GigDomainError.GIG_ID_CANNOT_BE_EMPTY;

@Builder
public record GigId(UUID id) implements Id {

    public GigId {
        id = Optional.of(id)
                .orElseThrow(() -> new BandDomainException(GIG_ID_CANNOT_BE_EMPTY));
    }

    public static GigId generate() {
        return GigId.fromUUID(UUID.randomUUID());
    }

    public static GigId fromUUID(UUID uuid) {
        return GigId.builder()
                .id(uuid)
                .build();
    }

    public static GigId fromString(String value) {
        return GigId.fromUUID(UUID.fromString(value));
    }
}
