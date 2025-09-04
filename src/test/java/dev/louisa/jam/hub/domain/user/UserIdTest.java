package dev.louisa.jam.hub.domain.user;

import dev.louisa.jam.hub.domain.IdTest;
import dev.louisa.jam.hub.domain.user.exceptions.UserDomainException;
import dev.louisa.jam.hub.exceptions.JamHubException;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.louisa.jam.hub.domain.user.exceptions.UserDomainError.*;

class UserIdTest extends IdTest<UserId> {
    @Override
    protected Supplier<UserId> generator() {
        return UserId::generate;
    }

    @Override
    protected Function<UUID, UserId> fromUuid() {
        return UserId::fromUUID;
    }

    @Override
    protected Function<String, UserId> fromString() {
        return UserId::fromString;
    }

    @Override
    protected Class<? extends JamHubException> getEmptyValueExceptionClass() {
        return UserDomainException.class;
    }

    @Override
    protected String getEmptyValueMessage() {
        return USER_ID_CANNOT_BE_EMPTY.getMessage();
    }
}