package dev.louisa.jam.hub.usecase;

import dev.louisa.jam.hub.domain.band.Band;
import dev.louisa.jam.hub.domain.band.BandId;
import dev.louisa.jam.hub.domain.band.BandMember;
import dev.louisa.jam.hub.domain.band.BandRole;
import dev.louisa.jam.hub.domain.gig.ExternalRole;
import dev.louisa.jam.hub.domain.gig.Gig;
import dev.louisa.jam.hub.domain.gig.GigId;
import dev.louisa.jam.hub.domain.gig.GigStatus;
import dev.louisa.jam.hub.domain.user.UserId;
import dev.louisa.jam.hub.domain.band.persistence.BandRepository;
import dev.louisa.jam.hub.domain.gig.persistence.GigRepository;
import dev.louisa.jam.hub.domain.shared.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SomeRandomTestsIT extends BaseIT {

    @Autowired
    private BandRepository bandRepository;
    @Autowired
    private GigRepository gigRepository;

    @Test
    void shouldSaveBand() {
        Band band = Band.builder()
                .id(BandId.generate())
                .name("The Rolling Codes")
                .build();

        bandRepository.save(band);

        Optional<Band> expectedBand = bandRepository.findById(band.getId());
        assertThat(expectedBand).isPresent();

        band.addMember(
                BandMember.builder()
                        .userId(UUID.randomUUID())
                        .role(BandRole.VOCALIST)
                        .build());

        band.addMember(
                BandMember.builder()
                        .userId(UUID.randomUUID())
                        .role(BandRole.GUITARIST)
                        .build());

        bandRepository.save(band);

        expectedBand = bandRepository.findById(band.getId());
        assertThat(expectedBand).isPresent();
        assertThat(expectedBand.get().getMembers().get(0).getId()).isNotNull();
    }

    @Test
    void shouldSaveGig() {
        Gig gig = Gig.builder()
                .id(GigId.generate())
                .title("Summer Fest 2024")
                .venueAddress(Address.builder()
                        .street("Main St")
                        .number(13L)
                        .city("Oakville")
                        .postalCode("12345")
                        .build())
                .bandId(UUID.randomUUID())
                .status(GigStatus.OPTION)
                .build();
        
        gigRepository.save(gig);
        
        Optional<Gig> expectedGig = gigRepository.findById(gig.getId());
        assertThat(expectedGig).isPresent();
        
        gig.promote();
        gigRepository.save(gig);
        
        expectedGig = gigRepository.findById(gig.getId());
        assertThat(expectedGig).isPresent();
        assertThat(expectedGig.get().getStatus()).isEqualTo(GigStatus.CONFIRMED);
        
        gig.assignRole(UserId.generate(), ExternalRole.STAGE_MANAGER);
        gigRepository.save(gig);

        expectedGig = gigRepository.findById(gig.getId());
        assertThat(expectedGig).isPresent();
        assertThat(expectedGig.get().getAssignments().get(0).getId()).isNotNull();
    }
}