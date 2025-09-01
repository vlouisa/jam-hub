package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.shared.Id;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Value
@Embeddable
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class BandId implements Id {
    UUID id;

    public static BandId generate() {
        return generate(UUID.randomUUID());
    }

    public static BandId generate(UUID uuid) {
        return new BandId(uuid);
    }
}
