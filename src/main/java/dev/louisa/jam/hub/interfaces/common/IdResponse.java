package dev.louisa.jam.hub.interfaces.common;

import dev.louisa.jam.hub.domain.common.Id;

import java.util.UUID;

public record IdResponse(UUID id) {
    public static IdResponse from(Id id){
        return new IdResponse(id.id());
    }
}
