package dev.louisa.jam.hub.domain.shared;

public class Validator<T> {
    private final T value;

    private Validator(T value) {
        this.value = value;
    }

    public static <T> Validator<T> validate(T value) {
        return new Validator<>(value);
    }

    public void ifNull(Runnable action) {
        if (value == null) {
            action.run();
        }
    }

    public void ifNullThrow(RuntimeException exception) {
        if (value == null) {
            throw exception;
        }
    }

    public void ifNullOrEmptyThrow(RuntimeException exception) {
        if (value == null
                || (value instanceof String && ((String) value).isEmpty())
                || (value instanceof java.util.Collection && ((java.util.Collection<?>) value).isEmpty())
                || (value instanceof java.util.Map && ((java.util.Map<?, ?>) value).isEmpty())) {
            throw exception;
        }
    }
}
