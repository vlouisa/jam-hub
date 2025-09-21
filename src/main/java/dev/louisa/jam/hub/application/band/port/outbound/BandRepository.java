package dev.louisa.jam.hub.application.band.port.outbound;

import dev.louisa.jam.hub.domain.band.Band;
import dev.louisa.jam.hub.domain.band.BandId;

import java.util.Optional;

public interface BandRepository {

    Band save(Band band);

    Optional<Band> findById(BandId id);

    Optional<Band> findByName(String name);
}