package dev.louisa.jam.hub.exceptions;

import dev.louisa.jam.hub.domain.BaseDomainTest;
import dev.louisa.jam.hub.domain.band.exceptions.BandDomainError;
import dev.louisa.jam.hub.domain.gig.exceptions.GigDomainError;
import dev.louisa.jam.hub.domain.shared.Id;
import dev.louisa.jam.hub.domain.user.exceptions.UserDomainError;
import lombok.Builder;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class JamHubExceptionTest extends BaseDomainTest {

    private static final Pattern FULL_MESSAGE_REGEX = Pattern.compile("^[A-Z]{3}-\\d{3} \\| [0-9]{3} .* \\| .*");


    @ParameterizedTest
    @ArgumentsSource(AllDomainErrorProvider.class)
    void messageIncludesErrorCodeAndMessage(JamHubError error) {
        TestException ex = new TestException(error, List.of(), null);

        assertThat(FULL_MESSAGE_REGEX.matcher(ex.getMessage()).matches()).isTrue();
        
        assertThat(ex.getMessage())
                .contains(error.getDomainCode() + "-" + error.getErrorCode())
                .contains(error.getMessage())
                .doesNotContain("Context:");
        
        assertThat(ex.getShortMessage()).isEqualTo(error.getMessage());
        assertThat(ex.getError()).isEqualTo(error);
        assertThat(ex.getContexts()).isEmpty();
        assertThat(ex.getHttpStatus()).isEqualTo(error.getHttpStatus());
        assertThat(ex.getCause()).isNull();
    }

    private static class AllDomainErrorProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            // List all DomainError enums manually or use reflection if package scanning is available
            Class<?>[] domainErrorEnums = {
                    BandDomainError.class,
                    GigDomainError.class,
                    UserDomainError.class
            };

            return Stream.of(domainErrorEnums)
                    .flatMap(clazz -> Stream.of(clazz.getEnumConstants()))
                    .map(Arguments::of);
        }
    }
    
    @ParameterizedTest
    @EnumSource(BandDomainError.class)
    void messageIncludesContexts(BandDomainError error) {
        Id context1 = MyId.fromString("4e3f1c2e-1d2b-4a5b-8c3d-123456789abc");
        Id context2 = MyId.fromString("7f6e5d4c-3b2a-1f0e-9d8c-987654321def");

        TestException ex = new TestException(error, List.of(context1, context2), null);

        assertThat(FULL_MESSAGE_REGEX.matcher(ex.getMessage()).matches()).isTrue();
        assertThat(ex.getMessage())
                .contains(error.getDomainCode() + "-" + error.getErrorCode())
                .contains(error.getMessage())
                .contains("Context: MyId[id=4e3f1c2e-1d2b-4a5b-8c3d-123456789abc], MyId[id=7f6e5d4c-3b2a-1f0e-9d8c-987654321def]");

        assertThat(ex.getShortMessage()).isEqualTo(error.getMessage());
        assertThat(ex.getError()).isEqualTo(error);
        assertThat(ex.getContexts()).containsExactly(context1, context2);
        assertThat(ex.getHttpStatus()).isEqualTo(error.getHttpStatus());
        assertThat(ex.getCause()).isNull();
    }

    @ParameterizedTest
    @EnumSource(BandDomainError.class)
    void messageIncludesCause(BandDomainError error) {
        var runtimeException = new RuntimeException("Root cause message");
        TestException ex = new TestException(error, List.of(), runtimeException);

        assertThat(FULL_MESSAGE_REGEX.matcher(ex.getMessage()).matches()).isTrue();
        assertThat(ex.getMessage())
                .contains(error.getDomainCode() + "-" + error.getErrorCode())
                .contains(error.getMessage())
                .doesNotContain("Context:");

        assertThat(ex.getShortMessage()).isEqualTo(error.getMessage());
        assertThat(ex.getError()).isEqualTo(error);
        assertThat(ex.getContexts()).isEmpty();
        assertThat(ex.getHttpStatus()).isEqualTo(error.getHttpStatus());
        assertThat(ex.getCause()).isEqualTo(runtimeException);
    }

    private static class TestException extends JamHubException {
        public TestException(JamHubError error, List<Id> contexts, Throwable cause) {
            super(error, contexts, cause);
        }
    }

    @Builder
    private record MyId(UUID id) implements Id {

        public MyId {
            id = Optional.of(id)
                    .orElseThrow(() -> new RuntimeException("ID cannot be null"));
        }

        public static MyId generate() {
            return MyId.fromUUID(UUID.randomUUID());
        }

        public static MyId fromUUID(UUID uuid) {
            return MyId.builder()
                    .id(uuid)
                    .build();
        }

        public static MyId fromString(String value) {
            return MyId.fromUUID(UUID.fromString(value));
        }
    }
}
