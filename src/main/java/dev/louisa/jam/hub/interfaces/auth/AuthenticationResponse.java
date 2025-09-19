package dev.louisa.jam.hub.interfaces.auth;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String jwt)  {}
