package dev.louisa.jam.hub.application.gig;

import dev.louisa.jam.hub.domain.band.Band;
import dev.louisa.jam.hub.domain.band.BandId;
import dev.louisa.jam.hub.domain.band.persistence.BandRepository;
import dev.louisa.jam.hub.domain.gig.Gig;
import dev.louisa.jam.hub.domain.gig.GigId;
import dev.louisa.jam.hub.domain.gig.persistence.GigRepository;
import dev.louisa.jam.hub.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GigApplicationService {

    private final BandRepository bandRepository;
    private final GigRepository gigRepository;

    @Transactional
    public GigId planGigForBand(UserId userId, BandId bandId, GigDetails details) {
        Band band = bandRepository.findById(bandId)
//                .orElseThrow(() -> new BandNotFoundException(bandId));
                .orElseThrow(() -> new RuntimeException("Band not found")); // Temporary exception handling

        // Check authorization
        if (!band.hasMember(userId)) {
//            throw new UnauthorizedException(userId);
            throw new RuntimeException("User not authorized"); // Temporary exception handling
        }

        // Create gig
        Gig gig = Gig.planNewGig(
                bandId,
                details
        );

        return gigRepository.save(gig).getId();
    }
}