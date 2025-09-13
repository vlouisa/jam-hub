package dev.louisa.jam.hub.infrastructure.event.listener;

import dev.louisa.jam.hub.domain.common.DomainEvent;

public interface DomainEventListener<T extends DomainEvent>  {
    void on(T event);
}
