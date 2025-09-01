package dev.louisa.jam.hub.interfaces.gig;

import dev.louisa.jam.hub.application.gig.GigDetails;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Builder
public record GigRequest(
        String title,
        AddressDto address,
        @NotBlank String gigDate,     // "28-12-2025"
        @NotBlank String getInTime,   // "17:30"
        @NotBlank String startTime,   // "20:00"
        @NotBlank String duration    // "01:30" → 1h30m
) {


    // Convert web-layer DTO (GigRequest) to application-layer DTO (GigDetails)
    public GigDetails toDetails() {
        LocalDate date = LocalDate.parse(gigDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalTime getIn = LocalTime.parse(getInTime, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime start = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));

        // Parse duration "HH:mm"
        String[] parts = duration.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        Duration parsedDuration = Duration.ofHours(hours).plusMinutes(minutes);

        return GigDetails.builder()
                .title(title)
                .address(address.toAddress())
                .eventDate(date)
                .getInTime(getIn)
                .startTime(start)
                .duration(parsedDuration)
                .build();
    }
}
