package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.domain.band.exceptions.BandDomainException;
import dev.louisa.jam.hub.domain.shared.Id;
import lombok.Builder;

import java.util.Optional;
import java.util.UUID;

import static dev.louisa.jam.hub.domain.band.exceptions.BandDomainError.BAND_ID_CANNOT_BE_EMPTY;

@Builder
public record BandId(UUID id) implements Id {

    public BandId {
        id = Optional.of(id)
                .orElseThrow(() -> new BandDomainException(BAND_ID_CANNOT_BE_EMPTY));
    }

    public static BandId generate() {
        return BandId.fromUUID(UUID.randomUUID());
    }

    public static BandId fromUUID(UUID uuid) {
        return BandId.builder()
                .id(uuid)
                .build();
    }

    public static BandId fromString(String value) {
        return BandId.fromUUID(UUID.fromString(value));
    }
}