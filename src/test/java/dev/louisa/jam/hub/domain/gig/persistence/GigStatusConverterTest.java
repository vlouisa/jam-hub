package dev.louisa.jam.hub.domain.gig.persistence;

import dev.louisa.jam.hub.domain.BaseDomainTest;
import dev.louisa.jam.hub.domain.gig.GigStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GigStatusConverterTest extends BaseDomainTest {
    private final GigStatusConverter converter = new GigStatusConverter();

    @Test
    void shouldConvertAllEnumValuesToDatabaseColumnAndBack() {
        for (GigStatus status : GigStatus.values()) {
            String dbValue = converter.convertToDatabaseColumn(status);
            assertThat(dbValue).isEqualTo(status.getCode());

            GigStatus convertedRole = converter.convertToEntityAttribute(dbValue);
            assertThat(convertedRole).isEqualTo(status);
        }
    }

    @Test
    void shouldReturnNullForNullRole() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
        assertThat(converter.convertToEntityAttribute(null)).isNull();
    }

}