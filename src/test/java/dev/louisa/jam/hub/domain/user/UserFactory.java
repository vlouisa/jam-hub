package dev.louisa.jam.hub.domain.user;

import dev.louisa.jam.hub.application.user.PasswordFactory;
import dev.louisa.jam.hub.domain.user.persistence.UserRepository;
import dev.louisa.jam.hub.domain.common.EmailAddress;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;

import java.util.function.Consumer;

@NoArgsConstructor
@AllArgsConstructor
public class UserFactory {

    private final Faker faker = new Faker();
    private PasswordFactory passwordFactory;

    public UserFactory usingPasswordFactory(PasswordFactory passwordFactory) {
        return new UserFactory(passwordFactory);
    }

    public User create() {
        return create(b -> {
        });
    }
    
    public User createWithEmail(String emailAddress) {
        return create(b -> b
                .email(EmailAddress.from(emailAddress)));
    }
    
    public User createWithCredentials(String emailAddress, String rawPassword) {
        if (passwordFactory == null) {
            return createWithEmail(emailAddress);
        }

        return create(b -> b
                .email(EmailAddress.from(emailAddress))
                .password(passwordFactory.from(rawPassword)));
    }

    public User create(Consumer<User.UserBuilder<?, ?>> customizer) {
        User.UserBuilder<?, ?> builder = baseBuilder();
        customizer.accept(builder);
        return builder.build();
    }

    // --- Switch into persistence mode ---
    public Persistent usingRepository(UserRepository repository) {
        if (repository == null) {
            throw new RuntimeException("Repository must not be null");
        }
        return new Persistent(repository);
    }

    public class Persistent {
        private final UserRepository repository;
        
        public Persistent(UserRepository repository) {
            this.repository = repository;
        }

        public User create() {
            return repository.save(UserFactory.this.create());
        }

        public User createWithEmail(String emailAddress) {
            return repository.save(UserFactory.this.createWithEmail(emailAddress));
        }
        
        public User createWithCredentials(String emailAddress, String rawPassword) {
            return repository.save(UserFactory.this.createWithCredentials(emailAddress, rawPassword));
        }

        public User create(Consumer<User.UserBuilder<?, ?>> customizer) {
            return repository.save(UserFactory.this.create(customizer));
        }
    }

    // --- Base builder with default/random values ---
    private User.UserBuilder<?, ?> baseBuilder() {
        EmailAddress email = EmailAddress.builder().email(faker.internet().emailAddress()).build();
        return User.builder()
                .id(UserId.generate())
                .email(email)
                .displayName(faker.name().fullName());
    }
}