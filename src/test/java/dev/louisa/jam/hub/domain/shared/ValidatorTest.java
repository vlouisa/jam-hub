package dev.louisa.jam.hub.domain.shared;

import dev.louisa.jam.hub.domain.BaseDomainTest;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class ValidatorTest extends BaseDomainTest {

    private static final RuntimeException NULL_VALUE_EXCEPTION = new RuntimeException("Null value");
    private static final RuntimeException SHOULD_NOT_RUN_EXCEPTION = new RuntimeException("Should not run");
    private static final RuntimeException SHOULD_NOT_THROW_EXCEPTION = new RuntimeException("Should not throw");
    private static final RuntimeException NULL_OR_EMPTY_EXCEPTION = new RuntimeException("Null or empty");

    @Test
    void ifNullShouldNotRunActionWhenValueIsNull() {
        Validator<String> validator = Validator.validate(null);

        assertThatCode(() -> validator.ifNull(() -> { throw NULL_VALUE_EXCEPTION;}))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Null value");
    }

    @Test
    void ifNullShouldNotRunActionWhenValueIsNotNull() {
        Validator<String> validator = Validator.validate("not null");
        
        assertThatCode(() -> validator.ifNull(() -> { throw SHOULD_NOT_RUN_EXCEPTION;}))
                .doesNotThrowAnyException();
    }

    @Test
    void ifNullThrowShouldThrowWhenValueIsNull() {
        Validator<String> validator = Validator.validate(null);
        
        assertThatCode(() -> validator.ifNullThrow(NULL_VALUE_EXCEPTION))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Null value");
    }

    @Test
    void ifNullThrowShouldThrowWhenValueIsNotNull() {
        Validator<String> validator = Validator.validate("not null");
        
        assertThatCode(() -> validator.ifNullThrow(SHOULD_NOT_THROW_EXCEPTION))
                .doesNotThrowAnyException();
    }

    @Test
    void ifNullOrEmptyShouldThrowWhenValueIsNull() {
        Validator<String> validator = Validator.validate(null);
        
        assertThatCode(() -> validator.ifNullOrEmptyThrow(NULL_OR_EMPTY_EXCEPTION))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Null or empty");
    }

    @Test
    void ifNullOrEmptyShouldThrowWhenStringIsEmpty() {
        Validator<String> validator = Validator.validate("");
        
        assertThatCode(() -> validator.ifNullOrEmptyThrow(NULL_OR_EMPTY_EXCEPTION))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Null or empty");
    }

    @Test
    void ifNullOrEmptyShouldThrowWhenCollectionIsEmpty() {
        Validator<Collection<String>> validator = Validator.validate(Collections.emptyList());
        
        assertThatCode(() -> validator.ifNullOrEmptyThrow(NULL_OR_EMPTY_EXCEPTION))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Null or empty");
    }

    @Test
    void ifNullOrEmptyShouldThrowWhenMapIsEmpty() {
        Validator<Map<String, String>> validator = Validator.validate(Collections.emptyMap());
        
        assertThatCode(() -> validator.ifNullOrEmptyThrow(NULL_OR_EMPTY_EXCEPTION))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Null or empty");
    }

    @Test
    void ifNullOrEmptyShouldNotThrowWhenStringIsNotEmpty() {
        Validator<String> validator = Validator.validate("abc");
        
        assertThatCode(() -> validator.ifNullOrEmptyThrow(SHOULD_NOT_THROW_EXCEPTION))
                .doesNotThrowAnyException();
    }

    @Test
    void ifNullOrEmptyShouldNotThrowWhenCollectionIsNotEmpty() {
        Validator<Collection<String>> validator = Validator.validate(List.of("a"));
        
        assertThatCode(() -> validator.ifNullOrEmptyThrow(SHOULD_NOT_THROW_EXCEPTION))
                .doesNotThrowAnyException();
    }

    @Test
    void ifNullOrEmptyShouldNotThrowWhenMapIsNotEmpty() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        Validator<Map<String, String>> validator = Validator.validate(map);
        
        assertThatCode(() -> validator.ifNullOrEmptyThrow(SHOULD_NOT_THROW_EXCEPTION))
                .doesNotThrowAnyException();
    }

    @Test
    void validateReturnsValueWhenValid() {
        String value = "test";
        Validator<String> validator = Validator.validate(value);
        
        assertThat(validator).isNotNull();
    }
}
