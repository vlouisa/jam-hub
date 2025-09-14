package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.testsupport.base.BaseDomainTest;
import dev.louisa.jam.hub.domain.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dev.louisa.jam.hub.testsupport.Factory.*;
import static org.assertj.core.api.Assertions.assertThat;

class BandTest extends BaseDomainTest {
    private Band band;

    @BeforeEach
    void setUp() {
        band = domain.aBand.create();
    }

    @Test
    void shouldAddMemberToTheBand() {
        BandMember member = domain.aBandMember.create();
        
        band.addMember(member);

        assertThat(band.getMembers()).contains(member);
        assertThat(member.getBand()).isEqualTo(band);
    }

    @Test
    void shouldRemoveMemberFromTheBand() {
        BandMember member = domain.aBandMember.create();
        
        band.addMember(member);
        band.removeMember(member);

        assertThat(band.getMembers()).doesNotContain(member);
        assertThat(member.getBand()).isNull();
    }

    @Test
    void shouldReturnTrueIfBandHasMember() {
        UserId userId = UserId.fromString("a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6");
        BandMember member = domain.aBandMember.create(u -> u.userId(userId.id()));

        band.addMember(member);

        assertThat(band.hasMember(userId)).isTrue();
    }

    @Test
    void shouldReturnFalseIfBandDoesNotHaveMember() {
        UserId userId = UserId.fromString("a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6");
        assertThat(band.hasMember(userId)).isFalse();
    }
}