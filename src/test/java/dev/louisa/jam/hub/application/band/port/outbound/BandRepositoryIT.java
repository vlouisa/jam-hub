package dev.louisa.jam.hub.application.band.port.outbound;

import dev.louisa.jam.hub.testsupport.base.BaseRepositoryIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.louisa.jam.hub.testsupport.Factory.domain.*;
import static org.assertj.core.api.Assertions.assertThat;

class BandRepositoryIT extends BaseRepositoryIT {
    @Autowired
    private BandRepository bandRepository;

    @Test
    void shouldSaveBandWithMembers() {
        var band = aBand.createWithMembers(3);

        bandRepository.save(band);

        var retrievedBand = bandRepository.findById(band.getId()).orElseThrow();
        assertThat(retrievedBand.getName()).isEqualTo(band.getName());
        assertThat(retrievedBand.getMembers())
                .hasSize(3)
                .allSatisfy(member -> assertThat(member.getBand()).isEqualTo(band));
    }

    @Test
    void shouldRetrieveBandById() {
        var band = aBand
                .usingRepository(bandRepository)
                .create();

        var retrievedBand = bandRepository.findById(band.getId()).orElseThrow();
        assertThat(retrievedBand.getName()).isEqualTo(band.getName());
    }

    @Test
    void shouldRetrieveBandByName() {
        var band = aBand
                .usingRepository(bandRepository)
                .create();

        var retrievedBand = bandRepository.findByName(band.getName()).orElseThrow();
        assertThat(retrievedBand.getId()).isEqualTo(band.getId());
    }
}