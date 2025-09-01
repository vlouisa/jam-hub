package dev.louisa.jam.hub.domain.gig;

import dev.louisa.jam.hub.shared.Id;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;


@Value
@Embeddable
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GigId implements Id {
    UUID id;
    
    public static GigId generate() {
        return generate(UUID.randomUUID());
    }

    public static GigId generate(UUID uuid) {
        return new GigId(uuid);
    }
}
