package dev.louisa.jam.hub.infrastructure.persistence.band;

import dev.louisa.jam.hub.domain.band.Band;
import dev.louisa.jam.hub.domain.band.BandId;
import dev.louisa.jam.hub.domain.band.persistence.BandRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaBandRepository extends JpaRepository<Band, BandId>, BandRepository {

    @Override
    Optional<Band> findByName(String name);
}