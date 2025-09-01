package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.shared.Id;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Value
@Embeddable
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BandId implements Id {
    UUID id;

    public static BandId generate() {
        return new BandId(UUID.randomUUID());
    }
}
