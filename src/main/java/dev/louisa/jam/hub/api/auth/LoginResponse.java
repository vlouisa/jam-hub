package dev.louisa.jam.hub.api.auth;

import lombok.Builder;

import java.util.UUID;

@Builder
public record LoginResponse(String accessToken, UUID userId)  {}
