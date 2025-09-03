package dev.louisa.jam.hub.application.gig;

import dev.louisa.jam.hub.application.BaseApplicationTest;
import dev.louisa.jam.hub.application.exceptions.ApplicationException;
import dev.louisa.jam.hub.domain.band.Band;
import dev.louisa.jam.hub.domain.band.BandId;
import dev.louisa.jam.hub.domain.band.persistence.BandRepository;
import dev.louisa.jam.hub.domain.gig.Gig;
import dev.louisa.jam.hub.domain.gig.GigId;
import dev.louisa.jam.hub.domain.gig.persistence.GigRepository;
import dev.louisa.jam.hub.domain.user.UserId;
import dev.louisa.jam.hub.testsupport.GigAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static dev.louisa.jam.hub.testsupport.Factory.application.*;
import static dev.louisa.jam.hub.testsupport.Factory.domain.*;
import static dev.louisa.jam.hub.domain.gig.GigStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GigApplicationServiceTest extends BaseApplicationTest {
    private static final UserId MICK = UserId.fromString("6f64a1d2-8e4b-4c3a-9b1e-1d2e3f4a5b6c");
    private static final UserId LENNY = UserId.fromString("da7c1e2b-3f4a-5b6c-7d8e-9f0a1b2c3d4e");
    private static final BandId ROLLING_STONES_ID = BandId.fromString("903fec94-652a-4ed5-aeaf-79d9181b9ffb");
    private Band rollingStones;

    @Mock
    private GigRepository gigRepository;
    @Mock
    private BandRepository bandRepository;

    private GigApplicationService gigApplicationService;


    @BeforeEach
    void setUp() {
        gigApplicationService = new GigApplicationService(bandRepository, gigRepository);
        rollingStones = aBand.create(b -> b.id(ROLLING_STONES_ID));
        aBandMember.forBand(rollingStones, m -> m.userId(MICK.id()));
    }

    @Test
    void shouldPlanNewGig() {
        var gig = aGig.create();
        var gigDetails = gigDetailsFactory.create();

        when(bandRepository.findById(rollingStones.getId())).thenReturn(Optional.of(rollingStones));
        when(gigRepository.save(any())).thenReturn(gig);

        final GigId gigId = gigApplicationService.planGigForBand(MICK, rollingStones.getId(), gigDetails);

        assertThat(gigId.toValue()).isEqualTo(gig.getId().toValue());

        ArgumentCaptor<Gig> captor = ArgumentCaptor.forClass(Gig.class);
        verify(gigRepository).save(captor.capture());

        final Gig savedGig = captor.getValue();
        GigAssert.assertThat(savedGig)
                .hasBandId(rollingStones.getId())
                .hasStatus(OPTION)
                .matchesDetails(gigDetails)
        ;
    }

    @Test
    void shouldThrowExceptionWhenBandNotFound() {
        var gigDetails = gigDetailsFactory.create();

        assertThatCode(() -> gigApplicationService.planGigForBand(MICK, rollingStones.getId(), gigDetails))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("APP-001 | 404 NOT_FOUND | Entity not found");

    }

    @Test
    void shouldThrowExceptionWhenNonBandMemberIsPlanningTheGig() {
        var gigDetails = gigDetailsFactory.create();
        when(bandRepository.findById(rollingStones.getId())).thenReturn(Optional.of(rollingStones));

        assertThatCode(() -> gigApplicationService.planGigForBand(LENNY, rollingStones.getId(), gigDetails))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("APP-100")
                .hasMessageContaining("403 FORBIDDEN")
                .hasMessageContaining("User not authorized for this action")
                .hasMessageContaining("UserId[id=da7c1e2b-3f4a-5b6c-7d8e-9f0a1b2c3d4e]")
                .hasMessageContaining("BandId[id=903fec94-652a-4ed5-aeaf-79d9181b9ffb]");
    }

}
