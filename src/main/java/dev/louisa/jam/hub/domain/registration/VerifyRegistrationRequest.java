package dev.louisa.jam.hub.domain.registration;

import lombok.Builder;

import java.util.UUID;

@Builder
public record VerifyRegistrationRequest(
        UUID otp,
        String password) {}
