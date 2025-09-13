package dev.louisa.jam.hub.infrastructure.persistence.user;

import dev.louisa.jam.hub.domain.common.DomainEvent;
import dev.louisa.jam.hub.domain.event.DomainEventPublisher;
import dev.louisa.jam.hub.domain.user.User;
import dev.louisa.jam.hub.domain.user.UserId;
import dev.louisa.jam.hub.domain.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EventPublishingJpaUserRepository implements UserRepository {
    private final JpaUserRepository delegate;
    private final DomainEventPublisher publisher;

    @Override
    public User save(User user) {
        final List<DomainEvent> events = user.pullDomainEvents();
        
        final User saved = delegate.save(user);
        events.forEach(publisher::publish);
        return saved;
    }

    @Override
    public Optional<User> findById(UserId id) {
        return delegate.findById(id);
    }
}