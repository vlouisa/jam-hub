package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.domain.user.UserId;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class BandMemberFactory {
    private final BandRole[] bandRoles = BandRole.values();
    
    public BandMember create() {
        return baseBuilder().build();
    }

    public BandMember create(Consumer<BandMember.BandMemberBuilder> customizer) {
        BandMember.BandMemberBuilder builder = baseBuilder();
        customizer.accept(builder);
        return builder.build();
    }

    /**
     * Create a random BandMember already assigned to a Band.
     * This ensures the relationship is consistent both ways.
     */
    public BandMember forBand(Band band) {
        BandMember member = create();
        band.addMember(member); // keeps aggregate invariant
        return member;
    }

    public BandMember forBand(Band band, Consumer<BandMember.BandMemberBuilder> customizer) {
        BandMember member = create(customizer);
        band.addMember(member);
        return member;
    }

    private BandMember.BandMemberBuilder baseBuilder() {
        return BandMember.builder()
                .id(UUID.randomUUID())
                .userId(UserId.generate().id())
                .role(randomBandRole())
                .recordCreationDateTime(Instant.now())
                .recordCreationUser(UUID.randomUUID())
                .recordModificationDateTime(Instant.now())
                .recordModificationUser(UUID.randomUUID());
    }

    private BandRole randomBandRole() {
        int index = ThreadLocalRandom.current().nextInt(bandRoles.length);
        return bandRoles[index];
    }
}
