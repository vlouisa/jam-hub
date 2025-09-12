package dev.louisa.jam.hub.infrastructure.aop;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Async
@EventListener
public @interface AsyncEventListener {

    /**
     * Alias for the classes this listener listens for (same as @EventListener.value)
     */
    Class<?>[] value() default {};

}