package dev.louisa.jam.hub.domain.gig;

import dev.louisa.jam.hub.testsupport.BaseDomainTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class ExternalRoleTest extends BaseDomainTest {
    @Test
    void allEnumValuesShouldHaveNonEmptyCode() {
        for (ExternalRole role : ExternalRole.values()) {
            assertThat(role.getCode())
                    .isNotNull()
                    .isNotEmpty();
        }
    }

    @Test
    void allEnumCodesShouldBeUnique() {
        Set<String> codes = new HashSet<>();
        for (ExternalRole role : ExternalRole.values()) {
            assertThat(codes).doesNotContain(role.getCode());
            codes.add(role.getCode());
        }
    }

    @Test
    void fromCodeShouldReturnCorrectExternalRole() {
        for (ExternalRole role : ExternalRole.values()) {
            assertThat(ExternalRole.fromCode(role.getCode())).isEqualTo(role);
        }
    }

    @Test
    void fromCodeShouldThrowExceptionForUnknownCode() {
        assertThatCode(() -> ExternalRole.fromCode("INVALID"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown code")
                .hasMessageContaining("INVALID");
    }
}