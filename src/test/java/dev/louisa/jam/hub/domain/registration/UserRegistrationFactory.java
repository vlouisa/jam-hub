package dev.louisa.jam.hub.domain.registration;

import dev.louisa.jam.hub.domain.registration.persistence.UserRegistrationRepository;
import dev.louisa.jam.hub.domain.common.EmailAddress;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;

public class UserRegistrationFactory {
    private final Faker faker = new Faker();
    
    // Fully random registration
    public UserRegistration create() {
        return create(user -> {});
    }

    // Random registration with customization
    public UserRegistration create(Consumer<UserRegistration.UserRegistrationBuilder> customizer) {
        UserRegistration.UserRegistrationBuilder builder = baseBuilder();
        customizer.accept(builder);
        return builder.build();
    }

    // Convenience methods
    public UserRegistration createVerified() {
        return create(registration -> registration.verifiedAt(Instant.now()));
    }

    public UserRegistration createRevoked() {
        return create(registration -> registration.revokedAt(Instant.now()));
    }

    public UserRegistration createExpired() {
        return create(registration -> registration.expiredAt(Instant.now().minusSeconds(3600)));
    }
    
    public Persistent usingRepository(UserRegistrationRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository must not be null");
        }
        return new Persistent(repository);
    }
    
    @RequiredArgsConstructor
    public class Persistent {
        private final UserRegistrationRepository repository;

        public UserRegistration create() {
            return repository.save(UserRegistrationFactory.this.create());
        }

        public UserRegistration create(Consumer<UserRegistration.UserRegistrationBuilder> customizer) {
            return repository.save(UserRegistrationFactory.this.create(customizer));
        }
        
        public UserRegistration createVerified() {
            return repository.save(UserRegistrationFactory.this.createVerified());
        }
        public UserRegistration createRevoked() {
            return repository.save(UserRegistrationFactory.this.createRevoked());
        }
        public UserRegistration createExpired() {
            return repository.save(UserRegistrationFactory.this.createExpired());
        }
    }
    
    // Base builder
    private UserRegistration.UserRegistrationBuilder baseBuilder() {
        return UserRegistration.builder()
                .id(UserRegistrationId.generate())
                .otp(UUID.randomUUID())
                .email(new EmailAddress(faker.internet().emailAddress()))
                .verifiedAt(null)
                .expiredAt(null)
                .revokedAt(null)
                .recordCreationDateTime(Instant.now())
                .recordCreationUser(UUID.randomUUID())
                .recordModificationDateTime(Instant.now())
                .recordModificationUser(UUID.randomUUID());
    }
}
