package dev.louisa.jam.hub.application.gig.port.outbound;

import dev.louisa.jam.hub.domain.gig.Gig;
import dev.louisa.jam.hub.domain.gig.GigId;

import java.util.Optional;

public interface GigRepository {

    Gig save(Gig gig);

    Optional<Gig> findById(GigId id);
}