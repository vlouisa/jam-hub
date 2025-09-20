package dev.louisa.jam.hub.api.auth;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String jwt)  {}
