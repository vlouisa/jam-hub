package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.domain.BaseDomainTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class BandRoleTest extends BaseDomainTest {

    @Test
    void allEnumValuesShouldHaveNonEmptyCode() {
        for (BandRole role : BandRole.values()) {
            assertThat(role.getCode())
                    .isNotNull()
                    .isNotEmpty();
        }
    }

    @Test
    void allEnumCodesShouldBeUnique() {
        Set<String> codes = new HashSet<>();
        for (BandRole role : BandRole.values()) {
            assertThat(codes).doesNotContain(role.getCode());
            codes.add(role.getCode());
        }
    }

    @Test
    void fromCodeShouldReturnCorrectBandRole() {
        for (BandRole role : BandRole.values()) {
            assertThat(BandRole.fromCode(role.getCode())).isEqualTo(role);
        }
    }

    @Test
    void fromCodeShouldThrowExceptionForUnknownCode() {
        assertThatCode(() -> BandRole.fromCode("INVALID"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown code")
                .hasMessageContaining("INVALID");
    }
}