package dev.louisa.jam.hub.domain.user;

import dev.louisa.jam.hub.application.auth.Password;
import dev.louisa.jam.hub.application.auth.port.outbound.PasswordHasher;
import dev.louisa.jam.hub.application.auth.port.outbound.UserRepository;
import dev.louisa.jam.hub.domain.common.EmailAddress;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

@NoArgsConstructor
@AllArgsConstructor
public class UserFactory {
    private final Faker faker = new Faker();
    private PasswordHasher passwordHasher;

    public UserFactory usingPasswordHasher(PasswordHasher passwordHasher) {
        return new UserFactory(passwordHasher);
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
        if (passwordHasher == null) {
            return createWithEmail(emailAddress);
        }

        return create(b -> b
                .email(EmailAddress.from(emailAddress))
                .hashedPassword(Password.fromString(rawPassword).hash(passwordHasher)));
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
        final String email = randomCustomEmail();
        final String displayName = email.substring(0, email.indexOf('@'));
        
        return User.builder()
                .id(UserId.generate())
                .email(EmailAddress.from(email))
                .displayName(displayName);
    }

    public String randomCustomEmail() {
        final List<String> CUSTOM_DOMAINS = List.of(
                "dinky-island.test", "mêlée-island.test", "scumm-bar.nl", "cannibal-village.me",
                "woodtick.org", "ville-de-la-booty.test", "bloody-lip-bar.org", "le-chuck.com",
                "the-voodoo-lounge.net", "the-pie-shop.org", "the-gilded-trout.com","the-scurvy-dog.net",
                "fluffysock.org","house-of-mojo.io","the-voodoo-lady.net","threepwood.test","grog-shop.com",
                "the-ink-pit.org", "the-kraken.net", "the-evil-wench.test", "the-leaning-tower-of-pizza.com",
                "the-melty-molars.test", "the-screaming-fish.net", "the-ghost-of-mcclintock.org",
                "the-plundered-pup.test", "the-sunken-treasure.org", "the-walrus-and-the-carpenter.net",
                "thimble-island.test", "tri-island.test", "isla-solos.test", "isla-de-muerta.test",
                "marley-mansion.org", "the-ship-of-fools.test", "the-jolly-buccaneer.net", "the-crow's-nest.org",
                "bluebanana.com","cloudytoast.net","maytheforcebewithyou.com","sendmorecoffee.net","mykeyboardissticky.org"
                );
        
        final var email = faker.internet().emailAddress();
        final var localPart = email.substring(0, email.indexOf('@'));
        final String domain = CUSTOM_DOMAINS.get(ThreadLocalRandom.current().nextInt(CUSTOM_DOMAINS.size()));
        return localPart + "@" + domain;
    }
}