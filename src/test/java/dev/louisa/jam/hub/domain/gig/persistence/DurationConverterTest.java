package dev.louisa.jam.hub.domain.gig.persistence;

import dev.louisa.jam.hub.testsupport.BaseDomainTest;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class DurationConverterTest extends BaseDomainTest {
    private final DurationConverter converter = new DurationConverter();

    @Test
    void shouldConvertAllEnumValuesToDatabaseColumnAndBack() {
        final Duration duration = Duration.ofHours(2);
        
        Long dbValue = converter.convertToDatabaseColumn(duration);
        assertThat(dbValue).isEqualTo(120);

        Duration convertedDuration = converter.convertToEntityAttribute(dbValue);
        assertThat(convertedDuration).isEqualTo(duration);
    }

    @Test
    void shouldReturnNullForNullRole() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
        assertThat(converter.convertToEntityAttribute(null)).isNull();
    }

}