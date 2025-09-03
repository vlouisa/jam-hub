package dev.louisa.jam.hub.application.gig;

import dev.louisa.jam.hub.domain.shared.Address;
import net.datafaker.Faker;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.function.Consumer;

public class GigDetailsFactory {
    private static final Faker faker = new Faker(new Locale("nl-NL"));

    public GigDetails create() {
        return create(gigDetailsBuilder -> {});
    }

    public GigDetails create(Consumer<GigDetails.GigDetailsBuilder> customizer) {
        GigDetails.GigDetailsBuilder builder = baseBuilder();
        customizer.accept(builder);
        return builder.build();
    }

    public GigDetails.GigDetailsBuilder baseBuilder() {
        
        return GigDetails.builder()
                .title(faker.rockBand().name() + " Live")
                .address(Address.builder()
                        .street(faker.address().streetName())
                        .number(Long.parseLong(faker.address().streetAddressNumber()))
                        .postalCode(faker.address().zipCode())
                        .city(faker.address().city())
                        .country(faker.address().country()) 
                        .build())
                .eventDate(LocalDate.now().plusDays(faker.number().numberBetween(1, 60)))
                .getInTime(LocalTime.of(faker.number().numberBetween(14, 17), 0))
                .startTime(LocalTime.of(faker.number().numberBetween(18, 23), 0))
                .duration(Duration.ofHours(faker.number().numberBetween(1, 3)));
    }
}