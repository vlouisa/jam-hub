package dev.louisa.jam.hub.testsupport.asserts;

import dev.louisa.jam.hub.application.gig.GigDetails;
import dev.louisa.jam.hub.domain.band.BandId;
import dev.louisa.jam.hub.domain.gig.Gig;
import dev.louisa.jam.hub.domain.gig.GigStatus;
import org.assertj.core.api.AbstractAssert;

public class GigAssert extends AbstractAssert<GigAssert, Gig> {

    private GigAssert(Gig actual) {
        super(actual, GigAssert.class);
    }

    public static GigAssert assertThatGig(Gig actual) {
        return new GigAssert(actual);
    }

    public GigAssert hasBandId(BandId expectedBandId) {
        isNotNull();
        if (!actual.getBandId().equals(expectedBandId.id())) {
            failWithMessage("Expected bandId <%s> but was <%s>", expectedBandId, actual.getBandId());
        }
        return this;
    }

    public GigAssert hasStatus(GigStatus expectedStatus) {
        isNotNull();
        if (!actual.getStatus().equals(expectedStatus)) {
            failWithMessage("Expected status <%s> but was <%s>", expectedStatus, actual.getStatus());
        }
        return this;
    }

    public GigAssert matchesDetails(GigDetails expectedDetails) {
        isNotNull();

        if (!actual.getTitle().equals(expectedDetails.title())) {
            failWithMessage("Expected title <%s> but was <%s>", expectedDetails.title(), actual.getTitle());
        }
        if (!actual.getEventDate().equals(expectedDetails.eventDate())) {
            failWithMessage("Expected eventDate <%s> but was <%s>", expectedDetails.eventDate(), actual.getEventDate());
        }
        if (!actual.getGetInTime().equals(expectedDetails.getInTime())) {
            failWithMessage("Expected getInTime <%s> but was <%s>", expectedDetails.getInTime(), actual.getGetInTime());
        }
        if (!actual.getStartTime().equals(expectedDetails.startTime())) {
            failWithMessage("Expected startTime <%s> but was <%s>", expectedDetails.startTime(), actual.getStartTime());
        }
        if (!actual.getDuration().equals(expectedDetails.duration())) {
            failWithMessage("Expected duration <%s> but was <%s>", expectedDetails.duration(), actual.getDuration());
        }
        if (!actual.getVenueAddress().equals(expectedDetails.address())) {
            failWithMessage("Expected address <%s> but was <%s>", expectedDetails.address(), actual.getVenueAddress());
        }

        return this;
    }
}
