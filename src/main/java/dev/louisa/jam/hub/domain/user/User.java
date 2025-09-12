package dev.louisa.jam.hub.domain.user;

import dev.louisa.jam.hub.domain.common.AuditableEntity;
import dev.louisa.jam.hub.domain.common.EmailAddress;
import dev.louisa.jam.hub.domain.common.Password;
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
public class User implements AuditableEntity {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private UserId id;

    @Column(nullable = false)
    private String displayName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "email", column = @Column(name = "email")),
    })
    private EmailAddress email;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "password", column = @Column(name = "password")),
    })
    private Password password;

    @CreationTimestamp
    private Instant recordCreationDateTime;
    private UUID recordCreationUser;
    @UpdateTimestamp
    private Instant recordModificationDateTime;
    private UUID recordModificationUser;
}
