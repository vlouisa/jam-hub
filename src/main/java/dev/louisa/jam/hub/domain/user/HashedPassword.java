package dev.louisa.jam.hub.domain.user;

import lombok.Builder;

import java.util.Objects;

@Builder
public record HashedPassword(String value) {

    public HashedPassword {
        Objects.requireNonNull(value, "Hash cannot be null");
    }
}