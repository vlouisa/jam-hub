package dev.louisa.jam.hub.domain.gig;

import dev.louisa.jam.hub.domain.gig.persistence.ExternalRoleConverter;
import dev.louisa.jam.hub.shared.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "jhb_gig_role_assignments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GigRoleAssignment implements AuditableEntity {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private UUID userId;

    @Convert(converter = ExternalRoleConverter.class)
    @Column(nullable = false) 
    private ExternalRole role;

    @ManyToOne
    @JoinColumn(name = "gig_id")
    private Gig gig;

    private Instant recordCreationDateTime;
    private String recordCreationUser;
    private Instant recordModificationDateTime;
    private String recordModificationUser;

}