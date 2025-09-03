package dev.louisa.jam.hub.domain.gig;

import dev.louisa.jam.hub.domain.shared.Address;
import dev.louisa.jam.hub.domain.user.UserId;
import net.datafaker.Faker;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import java.util.function.Consumer;

public class GigFactory {

    private static final Faker faker = new Faker();

    public Gig create() {
        return create(g -> {});
    }

    public Gig create(Consumer<Gig.GigBuilder> customizer) {
        Gig.GigBuilder builder = baseBuilder();
        customizer.accept(builder);
        return builder.build();
    }

    public GigRoleAssignment createAssignment(Gig gig, UserId userId, ExternalRole role) {
        GigRoleAssignment assignment = GigRoleAssignment.builder()
                .userId(userId.id())
                .role(role)
                .build();
        gig.getAssignments().add(assignment);
        assignment.setGig(gig);
        return assignment;
    }

    /**
     * Returns a pre-populated Gig builder with fake but valid data.
     */
    public Gig.GigBuilder baseBuilder() {
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
                .recordCreationUser(UUID.randomUUID().toString())
                .recordModificationDateTime(Instant.now())
                .recordModificationUser(UUID.randomUUID().toString());
    }
}
