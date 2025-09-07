package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.domain.band.persistence.BandRepository;
import dev.louisa.jam.hub.domain.shared.Guard;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BandFactory {
    private final Faker faker = new Faker();
    private final BandMemberFactory memberFactory = new BandMemberFactory();

    public Band create() {
        return create(b -> {});
    }

    public Band create(Consumer<Band.BandBuilder> customizer) {
        Band.BandBuilder builder = baseBuilder();
        customizer.accept(builder);
        return builder.build();
    }

    public Band createWithMembers(int memberCount) {
        return createWithMembers(memberCount, b -> {});
    }

    public Band createWithMembers(int memberCount, Consumer<Band.BandBuilder> customizer) {
        Band band = create(customizer);
        List<BandMember> members = randomMembers(memberCount);
        members.forEach(band::addMember);
        return band;
    }

    public Band createWithMembers( String... userIds) {
        return createWithMembers(b -> {}, userIds);
    }

    public Band createWithMembers(Consumer<Band.BandBuilder> customizer, String... userUUIDs) {
        Band band = create(customizer);
        for (String userUUID : userUUIDs) {
            BandMember member = memberFactory.create(u -> u.userId(UUID.fromString(userUUID)));
            band.addMember(member);
        }
        return band;
    }

    // --- Switch into persistence mode ---
    public Persistent usingRepository(BandRepository repository) {
        Guard.when(repository == null)
                .thenThrow(() -> new RuntimeException("Repository must not be null"));
        
        return new Persistent(repository);
    }

    @RequiredArgsConstructor
    public class Persistent {
        private final BandRepository repository;

        public Band create() {
            return repository.save(BandFactory.this.create());
        }

        public Band create(Consumer<Band.BandBuilder> customizer) {
            return repository.save(BandFactory.this.create(customizer));
        }

        public Band createWithMembers(int memberCount) {
            return repository.save(BandFactory.this.createWithMembers(memberCount));
        }

        public Band createWithMembers(int memberCount, Consumer<Band.BandBuilder> customizer) {
            return repository.save(BandFactory.this.createWithMembers(memberCount, customizer));
        }
        
        public  Band createWithMembers(String... userIds) {
            return repository.save(BandFactory.this.createWithMembers(userIds));
        }
        
        public Band createWithMembers(Consumer<Band.BandBuilder> customizer, String... userIds) {
            return repository.save(BandFactory.this.createWithMembers(customizer, userIds));
        }
    }
    
    private List<BandMember> randomMembers(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> memberFactory.create())
                .collect(Collectors.toList());
    }

    private Band.BandBuilder baseBuilder() {
        return Band.builder()
                .id(BandId.generate())
                .name(faker.rockBand().name())
                .recordCreationDateTime(Instant.now())
                .recordCreationUser(UUID.randomUUID())
                .recordModificationDateTime(Instant.now())
                .recordModificationUser(UUID.randomUUID());
    }
}
