package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.domain.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit-test")
class BandTest {

    private Band band;

    @BeforeEach
    void setUp() {
        band = Band.builder()
                .id(BandId.fromString("c3f1e8b2-9f1e-4d2a-8c3e-1e8b29f1e4d2"))
                .name("Maniac Mansion")
                .build();
    }

    @Test
    void shouldAddMemberToTheBand() {
        BandMember member = BandMember.builder()
                .id(UUID.fromString("d4e5f6a7-b8c9-0d1e-2f3a-4b5c6d7e8f90"))
                .userId(UUID.fromString("a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6"))
                .role(BandRole.GUITARIST)
                .build();

        band.addMember(member);

        assertThat(band.getMembers()).contains(member);
        assertThat(member.getBand()).isEqualTo(band);
    }

    @Test
    void shouldRemoveMemberFromTheBand() {
        BandMember member = BandMember.builder()
                .id(UUID.fromString("d4e5f6a7-b8c9-0d1e-2f3a-4b5c6d7e8f90"))
                .userId(UUID.fromString("a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6"))
                .role(BandRole.GUITARIST)
                .build();

        band.addMember(member);
        band.removeMember(member);

        assertThat(band.getMembers()).doesNotContain(member);
        assertThat(member.getBand()).isNull();
    }

    @Test
    void shouldReturnTrueIfBandHasMember() {
        UUID userId = UUID.fromString("a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6");
        BandMember member = BandMember.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .role(BandRole.DRUMMER)
                .build();

        band.addMember(member);

        assertThat(band.hasMember(UserId.fromUUID(userId))).isTrue();
    }

    @Test
    void shouldReturnFalseIfBandDoesNotHaveMember() {
        UUID userId = UUID.fromString("b43c2d1e-0f9e-8d7c-6b5a-4e3f2a1b0c9d");
        assertThat(band.hasMember(UserId.fromUUID(userId))).isFalse();
    }
}