package dev.louisa.jam.hub.application.gig.port.inbound;

import dev.louisa.jam.hub.application.gig.GigDetails;
import dev.louisa.jam.hub.domain.band.BandId;
import dev.louisa.jam.hub.domain.gig.GigId;
import dev.louisa.jam.hub.domain.user.UserId;

public interface PlanGig {
    GigId planNewGig(UserId userId, BandId bandId, GigDetails gigDetails);
}
