package dev.louisa.jam.hub.domain.registration;

import dev.louisa.jam.hub.domain.shared.EmailAddress;
import net.datafaker.Faker;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;

public class UserRegistrationFactory {
    private final Faker faker = new Faker();

    // Fully random registration
    public UserRegistration create() {
        return baseBuilder().build();
    }

    // Random registration with customization
    public UserRegistration create(Consumer<UserRegistration.UserRegistrationBuilder> customizer) {
        UserRegistration.UserRegistrationBuilder builder = baseBuilder();
        customizer.accept(builder);
        return builder.build();
    }

    // Convenience methods
    public UserRegistration createVerified() {
        return baseBuilder()
                .verifiedAt(Instant.now())
                .build();
    }

    public UserRegistration createRevoked() {
        return baseBuilder()
                .revokedAt(Instant.now())
                .build();
    }

    public UserRegistration createExpired() {
        return baseBuilder()
                .expiredAt(Instant.now().minusSeconds(3600)) // expired 1 hour ago
                .build();
    }

    // Base builder
    private UserRegistration.UserRegistrationBuilder baseBuilder() {
        return UserRegistration.builder()
                .id(UserRegistrationId.generate())
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
