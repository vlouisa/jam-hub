package dev.louisa.jam.hub.interfaces.gig;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class GigRequestFactory {

    private final Faker faker = new Faker();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public GigRequest create() {
        return create(builder -> {});
    }

    public GigRequest create(Consumer<GigRequest.GigRequestBuilder> customizer) {
        GigRequest.GigRequestBuilder builder = baseBuilder();
        customizer.accept(builder);
        return builder.build();
    }

    private GigRequest.GigRequestBuilder baseBuilder() {
        // Random date within next year
        LocalDate eventDate = LocalDate.now().plusDays(faker.number().numberBetween(1, 365));

        // Random get-in time between 14:00 - 19:00
        LocalTime getInTime = randomTime(14, 19);

        // Random start time 1-3 hours after get-in
        LocalTime startTime = getInTime.plusHours(faker.number().numberBetween(1, 3));

        // Random duration between 1 and 3 hours, format HH:mm
        int hours = faker.number().numberBetween(1, 3);
        int minutes = faker.options().option(0, 15, 30, 45);
        String duration = String.format("%02d:%02d", hours, minutes);

        return GigRequest.builder()
                .title(faker.rockBand().name() + " Fest")
                .gigDate(eventDate.format(dateFormatter))
                .getInTime(getInTime.format(timeFormatter))
                .startTime(startTime.format(timeFormatter))
                .duration(duration)
                .address(AddressDto.builder()
                        .street(faker.address().streetName() + " " + faker.number().numberBetween(1, 100))
                        .city(faker.address().city())
                        .postalCode(faker.address().zipCode())
                        .country(faker.address().country())
                        .build());
    }

    private LocalTime randomTime(int startHourInclusive, int endHourInclusive) {
        // Random hour between startHourInclusive and endHourInclusive
        int hour = ThreadLocalRandom.current().nextInt(startHourInclusive, endHourInclusive + 1);
        if (hour == endHourInclusive) {
            return LocalTime.of(hour, 0);
        }
        
        // Random minute from the allowed set {0, 15, 30, 45}
        int[] minutes = {0, 15, 30, 45};
        int minute = minutes[ThreadLocalRandom.current().nextInt(minutes.length)];

        return LocalTime.of(hour, minute);    }
}