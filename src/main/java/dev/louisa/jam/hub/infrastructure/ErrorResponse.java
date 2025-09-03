package dev.louisa.jam.hub.infrastructure;

import lombok.Builder;

import java.util.List;

@Builder
public record ErrorResponse(
        String errorCode,
        String message,
        List<String> context
){}
