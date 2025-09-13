package dev.louisa.jam.hub.interfaces.registration;

import lombok.Builder;

@Builder
public record VerifyRegistrationRequest(String password) {}
