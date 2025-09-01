package dev.louisa.jam.hub.domain.gig;

import dev.louisa.jam.hub.domain.gig.persistence.DurationConverter;
import dev.louisa.jam.hub.domain.gig.persistence.GigStatusConverter;
import dev.louisa.jam.hub.domain.user.UserId;
import dev.louisa.jam.hub.shared.Address;
import dev.louisa.jam.hub.shared.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "jhb_gigs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gig implements AuditableEntity {

    @EmbeddedId
    private GigId id;

    private String title;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "venue_address_street")),
            @AttributeOverride(name = "number", column = @Column(name = "venue_address_number")),
            @AttributeOverride(name = "city", column = @Column(name = "venue_address_city")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "venue_address_postal_code")),
            @AttributeOverride(name = "country", column = @Column(name = "venue_address_country"))
    })
    private Address venueAddress;

    private Instant getInTime;
    private Instant startTime;

    @Convert(converter = DurationConverter.class)
    private Duration duration;

    @Column(nullable = false)
    private UUID bandId;

    @Convert(converter = GigStatusConverter.class)
    private GigStatus status;

    // child entities (assignments)
    @OneToMany(mappedBy = "gig", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<GigRoleAssignment> assignments = new ArrayList<>();

    private Instant recordCreationDateTime;
    private String recordCreationUser;
    private Instant recordModificationDateTime;
    private String recordModificationUser;


    // --- `Domain Behaviour methods ---
    public void promote() {
        if (status != GigStatus.OPTION) {
            throw new IllegalStateException("Only 'options' can be promoted.");
        }
        this.status = GigStatus.CONFIRMED;
    }

    public void assignRole(UserId userId, ExternalRole role) {
        GigRoleAssignment assignment = GigRoleAssignment.builder()
                .userId(userId.getId())
                .role(role)
                .build();
        assignments.add(assignment);
        assignment.setGig(this);
    }
}