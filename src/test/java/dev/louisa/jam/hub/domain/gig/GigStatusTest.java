package dev.louisa.jam.hub.domain.gig;

import dev.louisa.jam.hub.testsupport.base.BaseDomainTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class GigStatusTest extends BaseDomainTest {
    @Test
    void allEnumValuesShouldHaveNonEmptyCode() {
        for (GigStatus status : GigStatus.values()) {
            assertThat(status.getCode())
                    .isNotNull()
                    .isNotEmpty();
        }
    }

    @Test
    void allEnumCodesShouldBeUnique() {
        Set<String> codes = new HashSet<>();
        for (GigStatus status : GigStatus.values()) {
            assertThat(codes).doesNotContain(status.getCode());
            codes.add(status.getCode());
        }
    }

    @Test
    void fromCodeShouldReturnCorrectGigStatus() {
        for (GigStatus status : GigStatus.values()) {
            assertThat(GigStatus.fromCode(status.getCode())).isEqualTo(status);
        }
    }

    @Test
    void fromCodeShouldThrowExceptionForUnknownCode() {
        assertThatCode(() -> GigStatus.fromCode("INVALID"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown code")
                .hasMessageContaining("INVALID");
    }

}