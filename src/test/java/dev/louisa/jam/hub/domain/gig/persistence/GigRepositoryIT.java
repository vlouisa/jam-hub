package dev.louisa.jam.hub.domain.gig.persistence;

import dev.louisa.jam.hub.testsupport.BaseIntegrationIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.louisa.jam.hub.testsupport.Factory.domain.aGig;
import static org.assertj.core.api.Assertions.assertThat;

class GigRepositoryIT extends BaseIntegrationIT {
    @Autowired
    private GigRepository gigRepository;

    @Test
    void shouldSaveGigWithAssignments() {
        var gig = aGig.createWithAssignments(4);

        gigRepository.save(gig);

        var retrievedGig = gigRepository.findById(gig.getId()).orElseThrow();
        assertThat(retrievedGig.getTitle()).isEqualTo(gig.getTitle());
        assertThat(retrievedGig.getAssignments())
                .hasSize(4)
                .allSatisfy(assignment -> assertThat(assignment.getGig()).isEqualTo(gig));
    }

    @Test
    void shouldRetrieveGigById() {
        var gig = aGig
                .usingRepository(gigRepository)
                .create();

        var retrievedGig = gigRepository.findById(gig.getId()).orElseThrow();
        assertThat(retrievedGig.getTitle()).isEqualTo(gig.getTitle());
    }
}