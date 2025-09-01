package dev.louisa.jam.hub.infrastructure.persistence.gig;

import dev.louisa.jam.hub.domain.gig.Gig;
import dev.louisa.jam.hub.domain.gig.GigId;
import dev.louisa.jam.hub.domain.gig.persistence.GigRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaGigRepository extends JpaRepository<Gig, GigId>, GigRepository {
}