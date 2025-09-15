package dev.louisa.jam.hub.domain.gig;

import dev.louisa.jam.hub.domain.gig.persistence.GigRepository;
import dev.louisa.jam.hub.domain.common.Address;
import dev.louisa.jam.hub.domain.common.Guard;
import dev.louisa.jam.hub.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GigFactory {
    private final Faker faker = new Faker();
    private final GigRoleAssignmentFactory roleAssignmentFactory = new GigRoleAssignmentFactory();

    public Gig create() {
        return create(g -> {
        });
    }

    public Gig create(Consumer<Gig.GigBuilder<?, ?>> customizer) {
        Gig.GigBuilder<?, ?> builder = baseBuilder();
        customizer.accept(builder);
        return builder.build();
    }

    public Gig createWithAssignments(int assignmentCount) {
        return createWithAssignments(assignmentCount, g -> {
        });
    }

    public Gig createWithAssignments(int assignmentCount, Consumer<Gig.GigBuilder<?, ?>> customizer) {
        Gig gig = create(customizer);
        List<GigRoleAssignment> assignments = randomAssignments(assignmentCount);
        assignments.forEach(a -> gig.assignRole(UserId.fromUUID(a.getUserId()), a.getRole()));
        return gig;
    }

    public Gig createWithAssignments(UserId... userIds) {
        return createWithAssignments(g -> {}, userIds);
    }

    public Gig createWithAssignments(Consumer<Gig.GigBuilder<?, ?>> customizer, UserId... userIds) {
        Gig gig = create(customizer);
        for (UserId userId : userIds) {
            GigRoleAssignment assignment = roleAssignmentFactory.create(a -> a.userId(userId.id()));
            gig.assignRole(userId, assignment.getRole());
        }
        return gig;
    }


    // --- Switch into persistence mode ---
    public Persistent usingRepository(GigRepository repository) {
        Guard.when(repository == null)
                .thenThrow(() -> new RuntimeException("Repository must not be null"));

        return new Persistent(repository);
    }

    @RequiredArgsConstructor
    public class Persistent {
        private final GigRepository repository;

        public Gig create() {
            return repository.save(GigFactory.this.create());
        }

        public Gig create(Consumer<Gig.GigBuilder<?, ?>> customizer) {
            return repository.save(GigFactory.this.create(customizer));
        }

        public Gig createWithAssignments(int assignmentCount) {
            return repository.save(GigFactory.this.createWithAssignments(assignmentCount));
        }

        public Gig createWithAssignments(int assignmentCount, Consumer<Gig.GigBuilder<?, ?>> customizer) {
            return repository.save(GigFactory.this.createWithAssignments(assignmentCount, customizer));
        }
        public Gig createWithAssignments(UserId... userIds) {
            return  repository.save(GigFactory.this.createWithAssignments(userIds));
        }
        public Gig createWithAssignments(Consumer<Gig.GigBuilder<?, ?>> customizer, UserId... userIds) {
            return repository.save(GigFactory.this.createWithAssignments(customizer, userIds));
        }
    }

    /**
     * Returns a pre-populated Gig builder with fake but valid data.
     */
    public Gig.GigBuilder<?, ?> baseBuilder() {
        return Gig.builder()
                .id(GigId.generate())
                .title(faker.rockBand().name() + " Live")
                .venueAddress(
                        Address.builder()
                                .street(faker.address().streetName())
                                .number(Long.parseLong(faker.address().streetAddressNumber()))
                                .city(faker.address().city())
                                .postalCode(faker.address().zipCode())
                                .country(faker.address().country())
                                .build())
                .eventDate(LocalDate.now().plusDays(faker.number().numberBetween(1, 60)))
                .getInTime(LocalTime.of(faker.number().numberBetween(14, 17), 0))
                .startTime(LocalTime.of(faker.number().numberBetween(18, 23), 0))
                .duration(Duration.ofHours(faker.number().numberBetween(1, 3)))
                .bandId(UUID.randomUUID())
                .status(faker.options().option(GigStatus.values()))
                .recordCreationDateTime(Instant.now())
                .recordCreationUser(UUID.randomUUID())
                .recordModificationDateTime(Instant.now())
                .recordModificationUser(UUID.randomUUID());
    }

    private List<GigRoleAssignment> randomAssignments(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> roleAssignmentFactory.create())
                .collect(Collectors.toList());
    }
}
