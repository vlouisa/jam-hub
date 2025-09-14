package dev.louisa.jam.hub.domain.common;

import lombok.Builder;

@Builder
public record EmailAddress(String email){
    public static EmailAddress from (String email){
        return EmailAddress.builder().email(email).build();
    }
}
