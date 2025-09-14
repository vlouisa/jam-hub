package dev.louisa.jam.hub.interfaces.gig;

import dev.louisa.jam.hub.domain.band.persistence.BandRepository;
import dev.louisa.jam.hub.domain.gig.GigId;
import dev.louisa.jam.hub.testsupport.base.BaseInterfaceIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.louisa.jam.hub.testsupport.Factory.domain.*;
import static dev.louisa.jam.hub.testsupport.Factory.web.*;
import static dev.louisa.jam.hub.testsupport.security.TokenCreator.create;
import static dev.louisa.jam.hub.testsupport.security.TokenCustomizer.withSubject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

class GigControllerIT extends BaseInterfaceIT {
    @Autowired
    private BandRepository bandRepository;

    @Test
    void shouldPlanNewGig() throws Exception {
        var band = aBand
                .usingRepository(bandRepository)
                .createWithMembers("b3aaf1f0-5e2e-4c3a-9f0a-1d2b3c4d5e6f");

        var gigId = api.post("/api/v1/bands/{bandId}/gigs", band.getId().toValue())
                .body(aGigRequest.create())
                .withJwt(create().aDefaultToken(withSubject("b3aaf1f0-5e2e-4c3a-9f0a-1d2b3c4d5e6f")))
                .expectResponseStatus(OK)
                .send()
                .andReturn(GigId.class);

        assertThat(gigId).isNotNull();
    }
}