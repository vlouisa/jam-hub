package dev.louisa.jam.hub.domain.user;

import dev.louisa.jam.hub.domain.BaseDomainTest;
import dev.louisa.jam.hub.domain.user.exceptions.UserDomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.UUID;

import static dev.louisa.jam.hub.domain.user.exceptions.UserDomainError.*;
import static org.assertj.core.api.Assertions.*;

class UserIdTest extends BaseDomainTest {
    @Test
    void shouldCreateValidId() {
        UserId id = UserId.builder()
                .id(UUID.randomUUID())
                .build();
        assertThat(id.id()).isNotNull();
    }

    @Test
    void shouldGenerateValidId() {
        UserId id = UserId.generate();
        assertThat(id.id()).isNotNull();
    }

    @Test
    void shouldCreateValidIdFromUuid() {
        UserId id = UserId.fromUUID(UUID.randomUUID());
        assertThat(id.id()).isNotNull();
    }

    @Test
    void shouldCreateValidIdFromString() {
        UserId id = UserId.fromString(UUID.randomUUID().toString());
        assertThat(id.id()).isNotNull();
    }

    @Test
    void shouldConvertUuidToString() {
        final UUID uuid = UUID.randomUUID();
        UserId id = UserId.fromUUID(uuid);

        assertThat(id.toValue()).isEqualTo(uuid.toString());
    }


    @Test
    void shouldBeEqualForSameUuid() {
        UUID uuid = UUID.randomUUID();
        UserId id1 = UserId.fromUUID(uuid);
        UserId id2 = UserId.fromUUID(uuid);

        assertThat(id1).isEqualTo(id2);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentUuids() {
        UserId id1 = UserId.fromUUID(UUID.randomUUID());
        UserId id2 = UserId.fromUUID(UUID.randomUUID());

        assertThat(id1).isNotEqualTo(id2);
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowForNullUuidWhenParsingFromUuid(UUID uuid) {
        assertThatCode(() -> UserId.fromUUID(uuid))
                .isInstanceOf(UserDomainException.class)
                .hasMessageContaining(USER_ID_CANNOT_BE_EMPTY.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowForEmptyValueWhenParsingFromString(String value) {
        assertThatCode(() -> UserId.fromString(value))
                .isInstanceOf(UserDomainException.class)
                .hasMessageContaining(USER_ID_CANNOT_BE_EMPTY.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowForNullUuid(UUID uuid) {
        assertThatThrownBy(() -> UserId.builder().id(uuid).build())
                .isInstanceOf(UserDomainException.class);
    }
}