package dev.louisa.jam.hub.application.exceptions;

import dev.louisa.jam.hub.domain.band.BandId;
import dev.louisa.jam.hub.domain.user.UserId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.louisa.jam.hub.application.exceptions.ApplicationError.ENTITY_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationExceptionTest {
    @Test
    void shouldFormatFullMessage() {
        var exception = new ApplicationException(ENTITY_NOT_FOUND);

        assertThat(exception.getCause()).isNull();
        assertThat(exception.getMessage())
                .isEqualTo("APP-001 | Entity not found");

    }

    @Test
    void shouldFormatFullMessageGivenOnlyContextProvided() {
        UserId userId = UserId.fromString("f9dd0882-b033-4e7c-bb62-a807aa576ee0");
        BandId bandId = BandId.fromString("33b7d3a8-ced3-4d08-ad15-2886f46526ae");

        var exception = new ApplicationException(ENTITY_NOT_FOUND, List.of(userId, bandId));

        assertThat(exception.getCause()).isNull();
        assertThat(exception.getMessage())
                .isEqualTo("APP-001 | Entity not found | Context: UserId[id=f9dd0882-b033-4e7c-bb62-a807aa576ee0], BandId[id=33b7d3a8-ced3-4d08-ad15-2886f46526ae]");

    }

    @Test
    void shouldFormatFullMessageGivenOnlyCauseProvided() {
        final RuntimeException rootCauseException = new RuntimeException("Root cause message");

        var exception = new ApplicationException(ENTITY_NOT_FOUND, rootCauseException);

        assertThat(exception.getCause()).isEqualTo(rootCauseException);
        assertThat(exception.getMessage())
                .isEqualTo("APP-001 | Entity not found");
    }

    @Test
    void shouldFormatFullMessageGivenCauseAndContextProvided() {
        UserId userId = UserId.fromString("f9dd0882-b033-4e7c-bb62-a807aa576ee0");
        BandId bandId = BandId.fromString("33b7d3a8-ced3-4d08-ad15-2886f46526ae");
        RuntimeException rootCauseException = new RuntimeException("Root cause message");

        final ApplicationException exception = new ApplicationException(ENTITY_NOT_FOUND, List.of(userId, bandId), rootCauseException);

        assertThat(exception.getCause()).isEqualTo(rootCauseException);
        assertThat(exception.getMessage())
                .isEqualTo("APP-001 | Entity not found | Context: UserId[id=f9dd0882-b033-4e7c-bb62-a807aa576ee0], BandId[id=33b7d3a8-ced3-4d08-ad15-2886f46526ae]");
    }
}