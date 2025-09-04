package dev.louisa.jam.hub.domain.gig;

import dev.louisa.jam.hub.domain.gig.persistence.ExternalRoleConverter;
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
@Table(name = "jhb_gig_role_assignments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GigRoleAssignment implements AuditableEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private UUID userId;

    @Convert(converter = ExternalRoleConverter.class)
    @Column(nullable = false) 
    private ExternalRole role;

    @ManyToOne
    @JoinColumn(name = "gig_id")
    private Gig gig;

    @CreationTimestamp
    private Instant recordCreationDateTime;
    private UUID recordCreationUser;
    @UpdateTimestamp
    private Instant recordModificationDateTime;
    private UUID recordModificationUser;
}