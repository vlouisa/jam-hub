package dev.louisa.jam.hub.domain.common;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent<T extends Id> {
    UUID id();
    Instant occurredOn();
    T aggregateId();
}
