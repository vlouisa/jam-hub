package dev.louisa.jam.hub.domain.registration.event;

import dev.louisa.jam.hub.domain.common.DomainEvent;
import dev.louisa.jam.hub.domain.common.EmailAddress;
import dev.louisa.jam.hub.domain.registration.UserRegistrationId;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserVerifiedEvent(
        UserRegistrationId userRegistrationId,
        EmailAddress emailAddress
) implements DomainEvent<UserRegistrationId> {

    @Override
    public UUID id() {
        return UUID.randomUUID();
    }

    @Override
    public Instant occurredOn() {
        return Instant.now();
    }

    @Override
    public UserRegistrationId aggregateId() {
        return userRegistrationId;
    }
}
