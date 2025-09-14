package dev.louisa.jam.hub.domain.gig;

import dev.louisa.jam.hub.application.gig.GigDetails;
import dev.louisa.jam.hub.testsupport.base.BaseDomainTest;
import dev.louisa.jam.hub.domain.gig.exceptions.GigDomainException;
import dev.louisa.jam.hub.domain.common.Address;
import dev.louisa.jam.hub.domain.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import static dev.louisa.jam.hub.testsupport.Factory.*;
import static dev.louisa.jam.hub.domain.gig.ExternalRole.*;
import static dev.louisa.jam.hub.domain.gig.GigStatus.*;
import static dev.louisa.jam.hub.domain.gig.exceptions.GigDomainError.GIG_CANNOT_BE_PROMOTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class GigTest extends BaseDomainTest {
    private Gig gig;
    
    @BeforeEach
    void setUp() {
        gig = domain.aGig.create();
    }
    
    @Test
    void shouldPlanNewGig() {
        var band = domain.aBand.create();
        var gigDetails = GigDetails.builder()
                .title("My Gig")
                .eventDate(LocalDate.of(2024, 12, 31))
                .getInTime(LocalTime.of(20, 0))
                .startTime(LocalTime.of(21, 0))
                .duration(Duration.ofHours(2))
                .address(Address.builder()
                        .street("Main Street")
                        .number(123L)
                        .city("Anytown")
                        .postalCode("12345")
                        .country("Country")
                        .build())
                .build();
        
        gig = Gig.planNewGig(band.getId(),gigDetails);

        var expectedGig = Gig.builder()
                .id(gig.getId())
                .bandId(band.getId().id())
                .title(gig.getTitle())
                .venueAddress(gigDetails.address())
                .eventDate(gigDetails.eventDate())
                .getInTime(gigDetails.getInTime())
                .startTime(gigDetails.startTime())
                .duration(gigDetails.duration())
                .status(OPTION)
                .build();

        assertThat(gig)
                .usingRecursiveComparison()
                .isEqualTo(expectedGig); 
    }

    @Test
    void shouldPromoteGig() {
        gig = domain.aGig.create(g -> g.status(OPTION));
        
        gig.promote();

        assertThat(gig.getStatus()).isEqualTo(CONFIRMED);
    }

    @ParameterizedTest
    @CsvSource({
            "CONFIRMED",
            "CANCELED"
    })
    void shouldNotPromoteGig(GigStatus gigStatus) {
        gig = domain.aGig.create(g -> g.status(gigStatus));
        
        assertThatCode(() -> gig.promote())
                .isInstanceOf(GigDomainException.class)
                .hasMessageContaining(GIG_CANNOT_BE_PROMOTED.getMessage());
    }

    @Test
    void shouldAssignRolesToGig() {
        UserId userId = UserId.generate();
        
        gig.assignRole(userId, SOUND_ENGINEER);
        gig.assignRole(userId, LIGHTING_TECH);

        assertThat(gig.getAssignments()).hasSize(2);
        assertThat(gig.getAssignments())
                .extracting(GigRoleAssignment::getGig)
                .map(Gig::getId)
                .containsExactlyInAnyOrder(gig.getId(), gig.getId());
        assertThat(gig.getAssignments())
                .extracting(GigRoleAssignment::getUserId)
                .containsExactlyInAnyOrder(userId.id(), userId.id());
    }
}