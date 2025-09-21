package dev.louisa.jam.hub.application.auth;

import dev.louisa.jam.hub.domain.user.UserId;
import lombok.Builder;

@Builder
public record Authentication(String accessToken, UserId userId) {}
