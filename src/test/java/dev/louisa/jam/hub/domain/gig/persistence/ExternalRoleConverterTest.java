package dev.louisa.jam.hub.domain.gig.persistence;

import dev.louisa.jam.hub.domain.BaseDomainTest;
import dev.louisa.jam.hub.domain.gig.ExternalRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExternalRoleConverterTest extends BaseDomainTest {
    private final ExternalRoleConverter converter = new ExternalRoleConverter();

    @Test
    void shouldConvertAllEnumValuesToDatabaseColumnAndBack() {
        for (ExternalRole role : ExternalRole.values()) {
            String dbValue = converter.convertToDatabaseColumn(role);
            assertThat(dbValue).isEqualTo(role.getCode());

            ExternalRole convertedRole = converter.convertToEntityAttribute(dbValue);
            assertThat(convertedRole).isEqualTo(role);
        }
    }

    @Test
    void shouldReturnNullForNullRole() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
        assertThat(converter.convertToEntityAttribute(null)).isNull();
    }

}