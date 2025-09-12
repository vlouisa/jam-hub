package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.domain.band.persistence.BandRoleConverter;
import dev.louisa.jam.hub.domain.common.DomainEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "jhb_band_members")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class BandMember extends DomainEntity {
    @Column(nullable = false)
    private UUID userId;

    @Convert(converter = BandRoleConverter.class)
    @Column(nullable = false)
    private BandRole role;

    @ManyToOne
    @JoinColumn(name = "band_id")
    private Band band;
    
}