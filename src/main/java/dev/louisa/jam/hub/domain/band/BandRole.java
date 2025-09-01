package dev.louisa.jam.hub.domain.band;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BandRole {
    VOCALIST("VOC", "Vocalist"),
    GUITARIST("GTR", "Guitarist"),
    KEYBOARDIST("KEY", "Keyboardist"),
    BASS_PLAYER("BAS", "Bass Player"),
    DRUMMER("DRM", "Drummer"),
    HORNS("HRN", "Horn section"),
    BACKING_VOCALS("BVO", "Backing Vocals"),
    OTHER("OTH", "Other");

    private final String code;
    private final String description;

    public static BandRole fromCode(String code) {
        for (BandRole role : values()) {
            if (role.code.equals(code)) return role;
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
