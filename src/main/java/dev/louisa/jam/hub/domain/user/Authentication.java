package dev.louisa.jam.hub.domain.user;

import lombok.Builder;

@Builder
public record Authentication(String accessToken, UserId userId) {}
