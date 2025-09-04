package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.domain.BaseDomainTest;
import dev.louisa.jam.hub.domain.band.exceptions.BandDomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.UUID;

import static dev.louisa.jam.hub.domain.band.exceptions.BandDomainError.BAND_ID_CANNOT_BE_EMPTY;
import static org.assertj.core.api.Assertions.*;

class BandIdTest extends BaseDomainTest {
    @Test
    void shouldCreateValidId() {
        BandId id = BandId.builder()
                .id(UUID.randomUUID())
                .build();
        assertThat(id.id()).isNotNull();
    }

    @Test
    void shouldGenerateValidId() {
        BandId id = BandId.generate();
        assertThat(id.id()).isNotNull();
    }
    
    @Test
    void shouldCreateValidIdFromUuid() {
        BandId id = BandId.fromUUID(UUID.randomUUID());
        assertThat(id.id()).isNotNull();
    }

    @Test
    void shouldCreateValidIdFromString() {
        BandId id = BandId.fromString(UUID.randomUUID().toString());
        assertThat(id.id()).isNotNull();
    }

    @Test
    void shouldConvertUuidToString() {
        final UUID uuid = UUID.randomUUID();
        BandId id = BandId.fromUUID(uuid);
        
        assertThat(id.toValue()).isEqualTo(uuid.toString());
    }
    
    
    @Test
    void shouldBeEqualForSameUuid() {
        UUID uuid = UUID.randomUUID();
        BandId id1 = BandId.fromUUID(uuid);
        BandId id2 = BandId.fromUUID(uuid);
        
        assertThat(id1).isEqualTo(id2);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentUuids() {
        BandId id1 = BandId.fromUUID(UUID.randomUUID());
        BandId id2 = BandId.fromUUID(UUID.randomUUID());
        
        assertThat(id1).isNotEqualTo(id2);
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowForNullUuidWhenParsingFromUuid(UUID uuid) {
        assertThatCode(() -> BandId.fromUUID(uuid))
                .isInstanceOf(BandDomainException.class)
                .hasMessageContaining(BAND_ID_CANNOT_BE_EMPTY.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowForEmptyValueWhenParsingFromString(String value) {
        assertThatCode(() -> BandId.fromString(value))
                .isInstanceOf(BandDomainException.class)
                .hasMessageContaining(BAND_ID_CANNOT_BE_EMPTY.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowForNullUuid(UUID uuid) {
        assertThatThrownBy(() -> BandId.builder().id(uuid).build())
                .isInstanceOf(BandDomainException.class);
    }
    
}