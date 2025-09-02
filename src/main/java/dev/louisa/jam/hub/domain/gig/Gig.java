package dev.louisa.jam.hub.domain.gig;

import dev.louisa.jam.hub.application.gig.GigDetails;
import dev.louisa.jam.hub.domain.band.BandId;
import dev.louisa.jam.hub.domain.gig.exceptions.GigDomainException;
import dev.louisa.jam.hub.domain.gig.persistence.DurationConverter;
import dev.louisa.jam.hub.domain.gig.persistence.GigStatusConverter;
import dev.louisa.jam.hub.domain.user.UserId;
import dev.louisa.jam.hub.domain.shared.Address;
import dev.louisa.jam.hub.domain.shared.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dev.louisa.jam.hub.domain.gig.exceptions.GigDomainError.*;

@Getter
@Setter
@Entity
@Table(name = "jhb_gigs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Gig implements AuditableEntity {

    @EmbeddedId
    @EqualsAndHashCode.Include
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

    private LocalDate eventDate;
    private LocalTime getInTime;
    private LocalTime startTime;

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
    public static Gig planNewGig(BandId bandId, GigDetails details) {
        return Gig.builder()
                .id(GigId.generate())
                .bandId(bandId.id())
                .title(details.title())
                .venueAddress(details.address())
                .eventDate(details.eventDate())
                .getInTime(details.getInTime())
                .startTime(details.startTime())
                .duration(details.duration())
                .status(GigStatus.OPTION) // New gigs start as 'options'
                .build();
    }
    
    public void promote() {
        if (status != GigStatus.OPTION) {
            throw new GigDomainException(GIG_CANNOT_BE_PROMOTED);
        }
        this.status = GigStatus.CONFIRMED;
    }

    public void assignRole(UserId userId, ExternalRole role) {
        GigRoleAssignment assignment = GigRoleAssignment.builder()
                .id(UUID.randomUUID())
                .userId(userId.id())
                .role(role)
                .build();
        assignments.add(assignment);
        assignment.setGig(this);
    }

}