package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.domain.band.exceptions.BandDomainException;
import dev.louisa.jam.hub.domain.shared.Id;
import lombok.Builder;

import java.util.UUID;

import static dev.louisa.jam.hub.domain.band.exceptions.BandDomainError.BAND_ID_CANNOT_BE_EMPTY;
import static dev.louisa.jam.hub.domain.shared.Validator.validate;

@Builder
public record BandId(UUID id) implements Id {

    public BandId {
        validate(id)
                .ifNullThrow(new BandDomainException(BAND_ID_CANNOT_BE_EMPTY));
    }

    public static BandId generate() {
        return BandId.fromUUID(UUID.randomUUID());
    }

    public static BandId fromUUID(UUID uuid) {
        validate(uuid)
                .ifNullThrow(new BandDomainException(BAND_ID_CANNOT_BE_EMPTY));
        
        return BandId.builder()
                .id(uuid)
                .build();
    }

    public static BandId fromString(String value) {
        validate(value)
                .ifNullOrEmptyThrow(new BandDomainException(BAND_ID_CANNOT_BE_EMPTY));
        
        return BandId.fromUUID(UUID.fromString(value));
    }
}