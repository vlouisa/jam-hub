package dev.louisa.jam.hub.domain.registration.event;

import dev.louisa.jam.hub.domain.common.DomainEvent;
import dev.louisa.jam.hub.domain.common.EmailAddress;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record RegistrationCreatedEvent(
        EmailAddress emailAddress,
        UUID otp
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
