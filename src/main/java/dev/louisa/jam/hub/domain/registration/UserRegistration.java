package dev.louisa.jam.hub.domain.registration;

import dev.louisa.jam.hub.domain.common.AggregateRoot;
import dev.louisa.jam.hub.domain.registration.event.RegistrationCreatedEvent;
import dev.louisa.jam.hub.domain.registration.event.UserVerifiedEvent;
import dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainException;
import dev.louisa.jam.hub.domain.common.EmailAddress;
import dev.louisa.jam.hub.domain.common.Guard;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

import static dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainError.OTP_CODE_EXPIRED;
import static dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainError.OTP_CODE_REVOKED;
import static java.time.temporal.ChronoUnit.*;

@Getter
@Setter
@Entity
@Table(name = "jhb_user_registrations")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserRegistration extends AggregateRoot<UserRegistrationId> {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "email", column = @Column(name = "email")),
    })
    private EmailAddress email;

    @Column
    private UUID otp;

    @Column
    private Instant verifiedAt;
    @Column
    private Instant expiredAt;
    @Column
    private Instant revokedAt;

    public static UserRegistration createNewRegistration(EmailAddress emailAddress) {
        final UserRegistration registration = UserRegistration.builder()
                .id(UserRegistrationId.generate())
                .email(emailAddress)
                .otp(UUID.randomUUID())
                .expiredAt(Instant.now().plus(24, HOURS))
                .build();

        registration.recordDomainEvent(
                RegistrationCreatedEvent.builder()
                        .emailAddress(registration.getEmail())
                        .otp(registration.getOtp())
                        .build());

        return registration;
    }

    public void verify() {
        Guard.when(registrationExpired())
                .thenThrow(() -> new UserRegistrationDomainException(OTP_CODE_EXPIRED))
                .orWhen(registrationRevoked())
                .thenThrow(() -> new UserRegistrationDomainException(OTP_CODE_REVOKED));

        this.verifiedAt = Instant.now();

        recordDomainEvent(
                UserVerifiedEvent.builder()
                        .userRegistrationId(this.getId())
                        .emailAddress(this.getEmail())
                        .build());
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
