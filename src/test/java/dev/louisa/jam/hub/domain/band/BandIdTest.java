package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.domain.IdTest;
import dev.louisa.jam.hub.domain.band.exceptions.BandDomainException;
import dev.louisa.jam.hub.exceptions.JamHubException;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.louisa.jam.hub.domain.band.exceptions.BandDomainError.BAND_ID_CANNOT_BE_EMPTY;

class BandIdTest extends IdTest<BandId> {
    @Override
    protected Supplier<BandId> generator() {
        return BandId::generate;
    }

    @Override
    protected Function<UUID, BandId> fromUuid() {
        return BandId::fromUUID;
    }

    @Override
    protected Function<String, BandId> fromString() {
        return BandId::fromString;
    }

    @Override
    protected Class<? extends JamHubException> getEmptyValueExceptionClass() {
        return BandDomainException.class;
    }

    @Override
    protected String getEmptyValueMessage() {
        return BAND_ID_CANNOT_BE_EMPTY.getMessage();
    }
}