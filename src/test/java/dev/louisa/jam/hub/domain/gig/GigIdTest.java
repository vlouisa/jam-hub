package dev.louisa.jam.hub.domain.gig;

import dev.louisa.jam.hub.domain.BaseDomainTest;
import dev.louisa.jam.hub.domain.gig.exceptions.GigDomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.UUID;

import static dev.louisa.jam.hub.domain.gig.exceptions.GigDomainError.*;
import static org.assertj.core.api.Assertions.*;

class GigIdTest extends BaseDomainTest {
    @Test
    void shouldCreateValidId() {
        GigId id = GigId.builder()
                .id(UUID.randomUUID())
                .build();
        assertThat(id.id()).isNotNull();
    }

    @Test
    void shouldGenerateValidId() {
        GigId id = GigId.generate();
        assertThat(id.id()).isNotNull();
    }

    @Test
    void shouldCreateValidIdFromUuid() {
        GigId id = GigId.fromUUID(UUID.randomUUID());
        assertThat(id.id()).isNotNull();
    }

    @Test
    void shouldCreateValidIdFromString() {
        GigId id = GigId.fromString(UUID.randomUUID().toString());
        assertThat(id.id()).isNotNull();
    }

    @Test
    void shouldConvertUuidToString() {
        final UUID uuid = UUID.randomUUID();
        GigId id = GigId.fromUUID(uuid);

        assertThat(id.toValue()).isEqualTo(uuid.toString());
    }


    @Test
    void shouldBeEqualForSameUuid() {
        UUID uuid = UUID.randomUUID();
        GigId id1 = GigId.fromUUID(uuid);
        GigId id2 = GigId.fromUUID(uuid);

        assertThat(id1).isEqualTo(id2);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentUuids() {
        GigId id1 = GigId.fromUUID(UUID.randomUUID());
        GigId id2 = GigId.fromUUID(UUID.randomUUID());

        assertThat(id1).isNotEqualTo(id2);
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowForNullUuidWhenParsingFromUuid(UUID uuid) {
        assertThatCode(() -> GigId.fromUUID(uuid))
                .isInstanceOf(GigDomainException.class)
                .hasMessageContaining(GIG_ID_CANNOT_BE_EMPTY.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowForEmptyValueWhenParsingFromString(String value) {
        assertThatCode(() -> GigId.fromString(value))
                .isInstanceOf(GigDomainException.class)
                .hasMessageContaining(GIG_ID_CANNOT_BE_EMPTY.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowForNullUuid(UUID uuid) {
        assertThatThrownBy(() -> GigId.builder().id(uuid).build())
                .isInstanceOf(GigDomainException.class);
    }

}