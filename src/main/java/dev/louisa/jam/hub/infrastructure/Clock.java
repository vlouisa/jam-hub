package dev.louisa.jam.hub.infrastructure;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class Clock {
    public Instant now() {
        return Instant.now();
    }
}
