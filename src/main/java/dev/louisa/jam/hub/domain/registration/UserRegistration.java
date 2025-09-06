package dev.louisa.jam.hub.domain.registration;

import dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainException;
import dev.louisa.jam.hub.domain.shared.AuditableEntity;
import dev.louisa.jam.hub.domain.shared.EmailAddress;
import dev.louisa.jam.hub.domain.shared.Guard;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

import static dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainError.OTP_CODE_EXPIRED;
import static dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainError.OTP_CODE_REVOKED;

@Getter
@Setter
@Entity
@Table(name = "jhb_user_registrations")
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

    public void verify() {
        Guard.when(registrationExpired())
                .thenThrow(() -> new UserRegistrationDomainException(OTP_CODE_EXPIRED))
                .orWhen(registrationRevoked())
                .thenThrow(() -> new UserRegistrationDomainException(OTP_CODE_REVOKED));

        this.verifiedAt = Instant.now();

        // TODO: Publish domain event
    }

    public boolean isVerified() {
        return this.verifiedAt != null;
    }
    
    private boolean registrationRevoked() {
        return this.revokedAt != null;
    }

    private boolean registrationExpired() {
        return this.expiredAt != null && Instant.now().isAfter(this.expiredAt);
    }
}
