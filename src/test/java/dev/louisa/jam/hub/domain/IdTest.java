package dev.louisa.jam.hub.domain;

import dev.louisa.jam.hub.domain.shared.Id;
import dev.louisa.jam.hub.exceptions.JamHubException;
import dev.louisa.jam.hub.testsupport.BaseDomainTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.*;

public abstract class IdTest<T extends Id> extends BaseDomainTest {
    protected abstract Supplier<T> generator();      // e.g., UserId::generate
    protected abstract Function<UUID, T> fromUuid(); // e.g., UserId::fromUUID
    protected abstract Function<String, T> fromString(); // e.g., UserId::fromString
    
    protected abstract Class<? extends JamHubException> getEmptyValueExceptionClass(); // Exception for empty/null
    protected abstract String getEmptyValueMessage();   // Exception message for empty/null

    @Test
    void shouldCreateValidId() {
        UUID uuid = UUID.randomUUID();
        T id = fromUuid().apply(uuid);
        assertThat(id.id()).isNotNull();
    }

    @Test
    void shouldGenerateValidId() {
        T id = generator().get();
        assertThat(id.id()).isNotNull();
    }

    @Test
    void shouldCreateValidIdFromUuid() {
        UUID uuid = UUID.randomUUID();
        T id = fromUuid().apply(uuid);
        assertThat(id.id()).isNotNull();
    }

    @Test
    void shouldCreateValidIdFromString() {
        UUID uuid = UUID.randomUUID();
        T id = fromString().apply(uuid.toString());
        assertThat(id.id()).isNotNull();
    }

    @Test
    void shouldConvertUuidToString() {
        UUID uuid = UUID.randomUUID();
        T id = fromUuid().apply(uuid);
        assertThat(id.toValue()).isEqualTo(uuid.toString());
    }

    @Test
    void shouldBeEqualForSameUuid() {
        UUID uuid = UUID.randomUUID();
        T id1 = fromUuid().apply(uuid);
        T id2 = fromUuid().apply(uuid);

        assertThat(id1).isEqualTo(id2);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentUuids() {
        T id1 = fromUuid().apply(UUID.randomUUID());
        T id2 = fromUuid().apply(UUID.randomUUID());

        assertThat(id1).isNotEqualTo(id2);
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowForNullUuidWhenParsingFromUuid(UUID uuid) {
        assertThatCode(() -> fromUuid().apply(uuid))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(getEmptyValueMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowForEmptyValueWhenParsingFromString(String value) {
        assertThatCode(() -> fromString().apply(value))
                .isInstanceOf(getEmptyValueExceptionClass())
                .hasMessageContaining(getEmptyValueMessage());
    }
}
