package dev.louisa.jam.hub.domain.common;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {
    UUID id();
    Instant occurredOn();
}
