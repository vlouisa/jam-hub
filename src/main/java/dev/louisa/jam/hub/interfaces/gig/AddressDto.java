package dev.louisa.jam.hub.interfaces.gig;

import dev.louisa.jam.hub.domain.shared.Address;

public record AddressDto(
        String street,
        Long number,
        String city,
        String postalCode,
        String country
) {
    
    public Address toAddress() {
        return Address.builder()
                .street(this.street)
                .number(this.number)
                .city(this.city)
                .postalCode(this.postalCode)
                .country(this.country)
                .build();
    }
}
