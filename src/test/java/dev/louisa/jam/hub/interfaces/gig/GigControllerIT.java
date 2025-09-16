package dev.louisa.jam.hub.interfaces.gig;

import dev.louisa.jam.hub.domain.band.persistence.BandRepository;
import dev.louisa.jam.hub.domain.gig.GigId;
import dev.louisa.jam.hub.domain.gig.GigStatus;
import dev.louisa.jam.hub.domain.gig.persistence.GigRepository;
import dev.louisa.jam.hub.domain.user.persistence.UserRepository;
import dev.louisa.jam.hub.infrastructure.security.jwt.JwtKeys;
import dev.louisa.jam.hub.interfaces.common.IdResponse;
import dev.louisa.jam.hub.testsupport.base.BaseInterfaceIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static dev.louisa.jam.hub.testsupport.Factory.domain.*;
import static dev.louisa.jam.hub.testsupport.Factory.web.*;
import static dev.louisa.jam.hub.testsupport.asserts.GigAssert.*;
import static dev.louisa.jam.hub.testsupport.security.TokenCreator.create;
import static dev.louisa.jam.hub.testsupport.security.TokenCustomizer.*;
import static org.springframework.http.HttpStatus.*;

class GigControllerIT extends BaseInterfaceIT {
    @Autowired
    private BandRepository bandRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GigRepository gigRepository;
    @Autowired
    private JwtKeys jwtKeys;

    @Test
    void shouldPlanNewGig() throws Exception {
        var user = aUser.usingRepository(userRepository).create();
        var band = aBand.usingRepository(bandRepository).createWithMembers(user.getId());
        var gigRequest = aGigRequest.create();

        var idResponse = api.post("/api/v1/bands/{bandId}/gigs", band.getId().toValue())
                .body(gigRequest)
                .withJwt(
                        create()
                                .using(jwtKeys.activeKey().toPrivateKey())
                                .aToken(
                                        forUser(user).andThen(withBands(List.of(band)))))
                .expectResponseStatus(OK)
                .send()
                .andReturn(IdResponse.class);

        var retrievedGig = gigRepository.findById(GigId.fromUUID(idResponse.id())).orElseThrow();
        assertThatGig(retrievedGig)
                .hasBandId(band.getId())
                .hasStatus(GigStatus.OPTION)
                .matchesDetails(gigRequest.toDetails());
    }
}