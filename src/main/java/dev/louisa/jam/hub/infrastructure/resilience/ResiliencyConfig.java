package dev.louisa.jam.hub.infrastructure.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.CannotCreateTransactionException;

import java.time.Duration;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ResiliencyConfig {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(MeterRegistry meterRegistry) {
        var config =  CircuitBreakerConfig.custom()
                .slidingWindowType(COUNT_BASED)
                .slidingWindowSize(60)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(40)
                .waitDurationInOpenState(Duration.ofMillis(30000))
                .permittedNumberOfCallsInHalfOpenState(1)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .recordExceptions(CannotCreateTransactionException.class)
                .build();

        var registry = CircuitBreakerRegistry.of(config);

        // Create named circuit breakers BEFORE binding them to the meter registry
        registry.circuitBreaker("database-cb")
                .getEventPublisher()
                .onStateTransition(event -> log.info("Circuit breaker '{}' state change:  {}", 
                        event.getCircuitBreakerName(),
                        event.getStateTransition()));
        
        // This ensures that metrics are collected and are available in Micrometer / Actuator
        TaggedCircuitBreakerMetrics
                .ofCircuitBreakerRegistry(registry)
                .bindTo(meterRegistry);
        
        return registry;
    }    
}
