package dev.louisa.jam.hub.domain.common;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AggregateRoot<T extends Id> implements AuditableEntity {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private T id;

    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    @CreationTimestamp
    private Instant recordCreationDateTime;
    private UUID recordCreationUser;
    @UpdateTimestamp
    private Instant recordModificationDateTime;
    private UUID recordModificationUser;

    protected void recordDomainEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public List<DomainEvent> pullDomainEvents() {
        var events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }
}
