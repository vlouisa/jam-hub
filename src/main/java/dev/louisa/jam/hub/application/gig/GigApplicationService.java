package dev.louisa.jam.hub.application.gig;

import dev.louisa.jam.hub.application.exceptions.ApplicationException;
import dev.louisa.jam.hub.application.gig.port.inbound.PlanGig;
import dev.louisa.jam.hub.domain.band.Band;
import dev.louisa.jam.hub.domain.band.BandId;
import dev.louisa.jam.hub.application.band.port.outbound.BandRepository;
import dev.louisa.jam.hub.domain.gig.Gig;
import dev.louisa.jam.hub.domain.gig.GigId;
import dev.louisa.jam.hub.application.gig.port.outbound.GigRepository;
import dev.louisa.jam.hub.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.louisa.jam.hub.application.exceptions.ApplicationError.*;

@Service
@RequiredArgsConstructor
public class GigApplicationService implements PlanGig {

    private final BandRepository bandRepository;
    private final GigRepository gigRepository;

    @Transactional
    @Override
    public GigId planNewGig(UserId userId, BandId bandId, GigDetails details) {
        Band band = bandRepository.findById(bandId)
                .orElseThrow(() -> new ApplicationException(ENTITY_NOT_FOUND)); 

        // Check authorization
        if (!band.hasMember(userId)) {
            throw new ApplicationException(USER_NOT_AUTHORIZED, List.of(userId, bandId));
        }

        // Create gig
        Gig gig = Gig.planNewGig(
                bandId,
                details
        );

        return gigRepository.save(gig).getId();
    }
}