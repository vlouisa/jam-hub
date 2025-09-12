package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.domain.band.exceptions.BandDomainException;
import dev.louisa.jam.hub.domain.common.Guard;
import dev.louisa.jam.hub.domain.common.Id;
import lombok.Builder;

import java.util.UUID;

import static dev.louisa.jam.hub.domain.band.exceptions.BandDomainError.BAND_ID_CANNOT_BE_EMPTY;

@Builder
public record BandId(UUID id) implements Id {
    private static final BandDomainException EMPTY_ID_EXCEPTION = new BandDomainException(BAND_ID_CANNOT_BE_EMPTY);

    public BandId {
        Guard.when(id == null)
                .thenThrow(EMPTY_ID_EXCEPTION);
    }

    public static BandId generate() {
        return BandId.fromUUID(UUID.randomUUID());
    }

    public static BandId fromUUID(UUID uuid) {
        Guard.when(uuid == null)
                .thenThrow(EMPTY_ID_EXCEPTION);
        
        return BandId.builder()
                .id(uuid)
                .build();
    }

    public static BandId fromString(String value) {
        Guard.when(value == null || value.isBlank())
                .thenThrow(EMPTY_ID_EXCEPTION);
        
        return BandId.fromUUID(UUID.fromString(value));
    }
}