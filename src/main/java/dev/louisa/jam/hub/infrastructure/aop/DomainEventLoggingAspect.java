package dev.louisa.jam.hub.infrastructure.aop;

import dev.louisa.jam.hub.domain.common.DomainEvent;
import dev.louisa.jam.hub.application.common.DomainEventListener;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class DomainEventLoggingAspect {

    @Around("@annotation(dev.louisa.jam.hub.infrastructure.aop.AsyncEventListener)")
    public Object logEventHandling(ProceedingJoinPoint pjp) throws Throwable {
        // Skip logging if the bean class does not implement EventListener
        if (!(pjp.getTarget() instanceof DomainEventListener)) {
            return pjp.proceed();
        }
        
        
        final DomainEvent event = (DomainEvent) pjp.getArgs()[0];
        log.info("Received event '{}', occurred on {}", event.getClass().getSimpleName(), event.occurredOn());
        Object result = pjp.proceed();
        log.info("Finished handling event '{}'", event.getClass().getSimpleName());

        return result;
    }
}