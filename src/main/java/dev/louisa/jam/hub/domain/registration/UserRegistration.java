package dev.louisa.jam.hub.domain.registration;

import dev.louisa.jam.hub.domain.common.AggregateRoot;
import dev.louisa.jam.hub.domain.registration.event.RegistrationCreatedEvent;
import dev.louisa.jam.hub.domain.registration.event.RegistrationVerifiedEvent;
import dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainException;
import dev.louisa.jam.hub.domain.common.EmailAddress;
import dev.louisa.jam.hub.domain.common.Guard;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

import static dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainError.*;
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
    private Instant expiresAt;
    @Column
    private Instant revokedAt;

    public static UserRegistration createNewRegistration(EmailAddress emailAddress) {
        final UserRegistration registration = UserRegistration.builder()
                .id(UserRegistrationId.generate())
                .email(emailAddress)
                .otp(UUID.randomUUID())
                .expiresAt(Instant.now().plus(24, HOURS))
                .build();

        registration.recordDomainEvent(
                RegistrationCreatedEvent.builder()
                        .userRegistrationId(registration.getId())
                        .emailAddress(registration.getEmail())
                        .otp(registration.getOtp())
                        .build());

        return registration;
    }

    public void verifyWithOtp(UUID otp) {
        Guard.when(registrationExpired()).thenThrow(() -> new UserRegistrationDomainException(OTP_CODE_EXPIRED))
                .orWhen(registrationRevoked()).thenThrow(() -> new UserRegistrationDomainException(OTP_CODE_REVOKED))
                .orWhen(!otp.equals(this.otp)).thenThrow(() -> new UserRegistrationDomainException(OTP_CODE_DOES_NOT_MATCH))
                .orWhen(isVerified()).thenThrow(() -> new UserRegistrationDomainException(OTP_CODE_ALREADY_VERIFIED));

        this.verifiedAt = Instant.now();

        recordDomainEvent(
                RegistrationVerifiedEvent.builder()
                        .userRegistrationId(this.getId())
                        .emailAddress(this.getEmail())
                        .build());
    }

    private boolean isVerified() {
        return this.verifiedAt != null;
    }

    private boolean registrationRevoked() {
        return this.revokedAt != null;
    }

    private boolean registrationExpired() {
        return this.expiresAt != null && Instant.now().isAfter(this.expiresAt);
    }
}
