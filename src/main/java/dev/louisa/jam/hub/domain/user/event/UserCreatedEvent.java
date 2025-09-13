package dev.louisa.jam.hub.domain.user.event;

import dev.louisa.jam.hub.domain.common.DomainEvent;
import dev.louisa.jam.hub.domain.common.EmailAddress;
import dev.louisa.jam.hub.domain.user.UserId;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserCreatedEvent(
        UserId userId,
        EmailAddress emailAddress
) implements DomainEvent {

    @Override
    public UUID id() {
        return UUID.randomUUID();
    }

    @Override
    public Instant occurredOn() {
        return Instant.now();
    }
}
