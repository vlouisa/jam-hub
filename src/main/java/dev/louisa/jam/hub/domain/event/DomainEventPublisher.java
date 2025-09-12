package dev.louisa.jam.hub.domain.event;

import dev.louisa.jam.hub.domain.common.DomainEvent;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}