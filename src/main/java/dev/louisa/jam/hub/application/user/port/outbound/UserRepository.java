package dev.louisa.jam.hub.application.user.port.outbound;

import dev.louisa.jam.hub.domain.common.EmailAddress;
import dev.louisa.jam.hub.domain.user.User;
import dev.louisa.jam.hub.domain.user.UserId;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UserId id);
    Optional<User> findByEmail(EmailAddress emailAddress);
}
