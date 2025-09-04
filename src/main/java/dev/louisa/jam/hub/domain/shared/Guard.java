package dev.louisa.jam.hub.domain.shared;

import dev.louisa.jam.hub.exceptions.JamHubException;
import java.util.function.Supplier;

public class Guard {

    private Guard() {}

    private boolean currentCondition;

    // Start a guard with a condition
    public static Guard when(boolean condition) {
        Guard guard = new Guard();
        guard.currentCondition = condition;
        return guard;
    }

    // Chain another condition
    public Guard orWhen(boolean condition) {
        this.currentCondition = condition;
        return this;
    }

    // Eager exception
    public Guard thenThrow(JamHubException exception) {
        if (currentCondition) {
            throw exception;
        }
        return this;
    }

    // Lazy exception
    public Guard thenThrow(Supplier<JamHubException> exceptionSupplier) {
        if (currentCondition) {
            throw exceptionSupplier.get();
        }
        return this;
    }
}