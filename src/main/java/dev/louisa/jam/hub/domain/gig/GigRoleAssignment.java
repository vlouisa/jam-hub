package dev.louisa.jam.hub.domain.gig;

import dev.louisa.jam.hub.domain.common.DomainEntity;
import dev.louisa.jam.hub.domain.gig.persistence.ExternalRoleConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "jhb_gig_role_assignments")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class GigRoleAssignment extends DomainEntity {

    @Column(nullable = false)
    private UUID userId;

    @Convert(converter = ExternalRoleConverter.class)
    @Column(nullable = false) 
    private ExternalRole role;

    @ManyToOne
    @JoinColumn(name = "gig_id")
    private Gig gig;
}