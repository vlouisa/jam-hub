package dev.louisa.jam.hub.api.gig;

import dev.louisa.jam.hub.domain.common.Address;
import lombok.Builder;

@Builder
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
