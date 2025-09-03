package dev.louisa.jam.hub.application.gig;

import dev.louisa.jam.hub.domain.shared.Address;
import lombok.Builder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record GigDetails (
        String title,
        Address address,
        LocalDate eventDate,
        LocalTime getInTime,
        LocalTime startTime,
        Duration duration
) {}
