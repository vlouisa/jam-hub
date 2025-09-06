package dev.louisa.jam.hub.domain.registration;

import dev.louisa.jam.hub.domain.IdTest;
import dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainException;
import dev.louisa.jam.hub.exceptions.JamHubException;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainError.USER_REGISTRATION_ID_CANNOT_BE_EMPTY;

class UserRegistrationIdTest extends IdTest<UserRegistrationId> {

    @Override
    protected Supplier<UserRegistrationId> generator() {
        return UserRegistrationId::generate;
    }

    @Override
    protected Function<UUID, UserRegistrationId> fromUuid() {
        return UserRegistrationId::fromUUID;
    }

    @Override
    protected Function<String, UserRegistrationId> fromString() {
        return UserRegistrationId::fromString;
    }

    @Override
    protected Class<? extends JamHubException> getEmptyValueExceptionClass() {
        return UserRegistrationDomainException.class;
    }


    @Override
    protected String getEmptyValueMessage() {
        return USER_REGISTRATION_ID_CANNOT_BE_EMPTY.getMessage();
    }
}