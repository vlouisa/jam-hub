package dev.louisa.jam.hub.domain.band.persistence;

import dev.louisa.jam.hub.testsupport.BaseDomainTest;
import dev.louisa.jam.hub.domain.band.BandRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BandRoleConverterTest extends BaseDomainTest {

    private final BandRoleConverter converter = new BandRoleConverter();

    @Test
    void shouldConvertAllEnumValuesToDatabaseColumnAndBack() {
        for (BandRole role : BandRole.values()) {
            String dbValue = converter.convertToDatabaseColumn(role);
            assertThat(dbValue).isEqualTo(role.getCode());

            BandRole convertedRole = converter.convertToEntityAttribute(dbValue);
            assertThat(convertedRole).isEqualTo(role);
        }
    }

    @Test
    void shouldReturnNullForNullRole() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
        assertThat(converter.convertToEntityAttribute(null)).isNull();
    }
}