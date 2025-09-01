package dev.louisa.jam.hub.domain.gig;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GigStatus {
    OPTION("OPT","Option"),
    CONFIRMED("CNF", "Confirmed"),
    CANCELED("CAN", "Canceled");

    private final String code;
    private final String description;

    public static GigStatus fromCode(String code) {
        for (GigStatus role : values()) {
            if (role.code.equals(code)) return role;
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}