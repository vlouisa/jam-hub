package dev.louisa.jam.hub.infrastructure.mail;

import lombok.Builder;

@Builder
public record EmailAddress(
        String address, 
        String personal) {
}
