package dev.louisa.jam.hub.infrastructure.aop;

import io.github.resilience4j.circuitbreaker.*;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CircuitBreakerAnnotationAspect {

    private final CircuitBreakerRegistry registry;

    @Around("@annotation(withCircuitBreaker)")
    public Object applyCircuitBreaker(ProceedingJoinPoint pjp, WithCircuitBreaker withCircuitBreaker) throws Throwable {
        CircuitBreaker breaker = registry.circuitBreaker(withCircuitBreaker.name());
        return breaker.executeCallable(() -> {
            try {
                return pjp.proceed();
            } catch (Throwable t) {
                if (t instanceof Exception) throw (Exception) t;
                else throw new RuntimeException(t);
            }
        });
    }
}