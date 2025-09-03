package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.domain.band.persistence.BandRoleConverter;
import dev.louisa.jam.hub.domain.shared.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "jhb_band_members")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BandMember implements AuditableEntity {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private UUID userId;

    @Convert(converter = BandRoleConverter.class)
    @Column(nullable = false)
    private BandRole role;

    @ManyToOne
    @JoinColumn(name = "band_id")
    private Band band;
    
    @CreationTimestamp
    private Instant recordCreationDateTime;
    private String recordCreationUser;
    @UpdateTimestamp
    private Instant recordModificationDateTime;
    private String recordModificationUser;

}