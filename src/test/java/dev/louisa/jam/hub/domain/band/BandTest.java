package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.domain.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit-test")
class BandTest {
    private final BandFactory bandFactory = new BandFactory();
    private final BandMemberFactory bandMemberFactory = new BandMemberFactory();

    private Band band;

    @BeforeEach
    void setUp() {
        band = bandFactory.create();
    }

    @Test
    void shouldAddMemberToTheBand() {
        BandMember member = bandMemberFactory.create();
        
        band.addMember(member);

        assertThat(band.getMembers()).contains(member);
        assertThat(member.getBand()).isEqualTo(band);
    }

    @Test
    void shouldRemoveMemberFromTheBand() {
        BandMember member = bandMemberFactory.create();
        
        band.addMember(member);
        band.removeMember(member);

        assertThat(band.getMembers()).doesNotContain(member);
        assertThat(member.getBand()).isNull();
    }

    @Test
    void shouldReturnTrueIfBandHasMember() {
        UserId userId = UserId.fromString("a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6");
        BandMember member = bandMemberFactory.create(u -> u.userId(userId.id()));

        band.addMember(member);

        assertThat(band.hasMember(userId)).isTrue();
    }

    @Test
    void shouldReturnFalseIfBandDoesNotHaveMember() {
        UserId userId = UserId.fromString("a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6");
        assertThat(band.hasMember(userId)).isFalse();
    }
}