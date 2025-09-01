package dev.louisa.jam.hub.domain.gig;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExternalRole {
    SOUND_ENGINEER("SE","Sound Engineer"),
    LIGHTING_TECH("LT", "Light Technician"),
    STAGEHAND("SH", "Stagehand"),
    STAGE_MANAGER("SM", "Stage Manager");

    private final String code;
    private final String description;

    public static ExternalRole fromCode(String code) {
        for (ExternalRole role : values()) {
            if (role.code.equals(code)) return role;
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}