package dev.louisa.jam.hub.domain.gig;

import dev.louisa.jam.hub.domain.user.UserId;
import net.datafaker.Faker;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;

public class GigRoleAssignmentFactory {

    private static final Faker faker = new Faker();


    public GigRoleAssignment create() {
        return create(a -> {});
    }

    public GigRoleAssignment create(Consumer<GigRoleAssignment.GigRoleAssignmentBuilder> customizer) {
        GigRoleAssignment.GigRoleAssignmentBuilder builder = baseBuilder();
        customizer.accept(builder);
        return builder.build();
    }

    /**
     * Creates an assignment and attaches it to a Gig.
     */
    public GigRoleAssignment createForGig(Gig gig) {
        return createForGig(gig, a -> {});
    }

    public GigRoleAssignment createForGig(Gig gig, Consumer<GigRoleAssignment.GigRoleAssignmentBuilder> customizer) {
        GigRoleAssignment assignment = create(customizer);
        gig.getAssignments().add(assignment);
        assignment.setGig(gig);
        return assignment;
    }

    /**
     * Pre-populated builder with fake data.
     */
    public GigRoleAssignment.GigRoleAssignmentBuilder baseBuilder() {
        return GigRoleAssignment.builder()
                .userId(UserId.generate().id())
                .role(faker.options().option(ExternalRole.values()))
                .recordCreationDateTime(Instant.now())
                .recordCreationUser(UUID.randomUUID())
                .recordModificationDateTime(Instant.now())
                .recordModificationUser(UUID.randomUUID());
    }
}