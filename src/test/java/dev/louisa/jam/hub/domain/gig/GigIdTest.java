package dev.louisa.jam.hub.domain.gig;

import dev.louisa.jam.hub.domain.IdTest;
import dev.louisa.jam.hub.domain.gig.exceptions.GigDomainException;
import dev.louisa.jam.hub.exceptions.JamHubException;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.louisa.jam.hub.domain.gig.exceptions.GigDomainError.*;

class GigIdTest extends IdTest<GigId> {

    @Override
    protected Supplier<GigId> generator() {
        return GigId::generate;
    }

    @Override
    protected Function<UUID, GigId> fromUuid() {
        return GigId::fromUUID;
    }

    @Override
    protected Function<String, GigId> fromString() {
        return GigId::fromString;
    }

    @Override
    protected Class<? extends JamHubException> getEmptyValueExceptionClass() {
        return GigDomainException.class;
    }

    @Override
    protected String getEmptyValueMessage() {
        return GIG_ID_CANNOT_BE_EMPTY.getMessage();
    }
}