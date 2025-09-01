package dev.louisa.jam.hub.domain.user;

import dev.louisa.jam.hub.shared.Id;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;


@Value
@Embeddable
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId implements Id {
    UUID id;
    
    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }
}
