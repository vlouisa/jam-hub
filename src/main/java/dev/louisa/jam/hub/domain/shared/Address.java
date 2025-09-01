package dev.louisa.jam.hub.domain.shared;

import lombok.Builder;

@Builder
public record Address (
    String street,
    Long number,
    String city,
    String postalCode,
    String country
){}
