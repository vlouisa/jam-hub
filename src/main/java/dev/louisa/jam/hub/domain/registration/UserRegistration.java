package dev.louisa.jam.hub.domain.registration;

import dev.louisa.jam.hub.domain.shared.AuditableEntity;
import dev.louisa.jam.hub.domain.shared.EmailAddress;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "jhb_users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserRegistration implements AuditableEntity {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private UserRegistrationId id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "email", column = @Column(name = "email")),
    })
    private EmailAddress email;

    @Column
    private Instant verifiedAt;
    @Column
    private Instant expiredAt;
    @Column
    private Instant revokedAt;

    @CreationTimestamp
    private Instant recordCreationDateTime;
    private UUID recordCreationUser;
    @UpdateTimestamp
    private Instant recordModificationDateTime;
    private UUID recordModificationUser;
}
