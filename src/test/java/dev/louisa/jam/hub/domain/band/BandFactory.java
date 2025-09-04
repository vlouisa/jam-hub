package dev.louisa.jam.hub.domain.band;

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
        return baseBuilder().build();
    }

    public Band create(Consumer<Band.BandBuilder> customizer) {
        Band.BandBuilder builder = baseBuilder();
        customizer.accept(builder);
        return builder.build();
    }

    public Band createWithMembers(int memberCount) {
        Band band = create();
        List<BandMember> members = randomMembers(memberCount);
        members.forEach(band::addMember); // ensures invariant is respected
        return band;
    }

    public Band createWithMembers(int memberCount, Consumer<Band.BandBuilder> customizer) {
        Band band = create(customizer);
        List<BandMember> members = randomMembers(memberCount);
        members.forEach(band::addMember);
        return band;
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
