package dev.louisa.jam.hub.infrastructure.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithCircuitBreaker {
    String name();
}
