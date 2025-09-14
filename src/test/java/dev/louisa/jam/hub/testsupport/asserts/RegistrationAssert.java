package dev.louisa.jam.hub.testsupport.asserts;

import dev.louisa.jam.hub.domain.common.EmailAddress;
import dev.louisa.jam.hub.domain.registration.UserRegistration;
import dev.louisa.jam.hub.domain.registration.UserRegistrationId;
import org.assertj.core.api.AbstractAssert;

import java.time.Instant;
import java.util.UUID;

public class RegistrationAssert extends AbstractAssert<RegistrationAssert, UserRegistration> {

    private RegistrationAssert(UserRegistration actual) {
        super(actual, RegistrationAssert.class);
    }

    public static RegistrationAssert assertThatRegistration(UserRegistration actual) {
        return new RegistrationAssert(actual);
    }

    public RegistrationAssert hasId(UserRegistrationId expectedId) {
        isNotNull();
        if (!actual.getId().equals(expectedId)) {
            failWithMessage("Expected id <%s> but was <%s>", expectedId, actual.getId());
        }
        return this;
    }

    public RegistrationAssert hasEmail(EmailAddress expectedEmail) {
        isNotNull();
        if (!actual.getEmail().equals(expectedEmail)) {
            failWithMessage("Expected email <%s> but was <%s>", expectedEmail, actual.getEmail());
        }
        return this;
    }

    public RegistrationAssert hasOtp(UUID expectedOtp) {
        isNotNull();
        if (!actual.getOtp().equals(expectedOtp)) {
            failWithMessage("Expected OTP <%s> but was <%s>", expectedOtp, actual.getOtp());
        }
        return this;
    }

    public RegistrationAssert hasExpiryAfter(Instant pointInTime) {
        isNotNull();
        if (!actual.getExpiresAt().isAfter(pointInTime)) {
            failWithMessage("Expected expiry after <%s> but was <%s>", pointInTime, actual.getExpiresAt());
        }
        return this;
    }

    public RegistrationAssert isVerified() {
        isNotNull();
        if (actual.getVerifiedAt() == null) {
            failWithMessage("Expected registration to be verified but it was not");
        }
        return this;
    }

    public RegistrationAssert isVerifiedAt(Instant pointInTime) {
        isNotNull();
        if (actual.getVerifiedAt() == null || !actual.getVerifiedAt().equals(pointInTime)) {
            failWithMessage("Expected registration to be verified at <%s> but was <%s>", pointInTime, actual.getVerifiedAt());
        }
        return this;
    }


        public RegistrationAssert isNotVerified() {
        isNotNull();
        if (actual.getVerifiedAt() != null) {
            failWithMessage("Expected registration not to be verified but it was");
        }
        return this;
    }

    public RegistrationAssert isRevoked() {
        isNotNull();
        if (actual.getRevokedAt() == null) {
            failWithMessage("Expected registration to be revoked but it was not");
        }
        return this;
    }

    public RegistrationAssert isNotRevoked() {
        isNotNull();
        if (actual.getRevokedAt() != null) {
            failWithMessage("Expected registration not to be revoked but it was revoked at <%s>", actual.getRevokedAt());
        }
        return this;
    }

    public RegistrationAssert isExpired() {
        isNotNull();
        if (actual.getExpiresAt() == null || Instant.now().isBefore(actual.getExpiresAt())) {
            failWithMessage("Expected registration to be expired but it was not. ExpiredAt = <%s>", actual.getExpiresAt());
        }
        return this;
    }

    public RegistrationAssert isNotExpired() {
        isNotNull();
        if (actual.getExpiresAt() != null && Instant.now().isAfter(actual.getExpiresAt())) {
            failWithMessage("Expected registration not to be expired but it expired at <%s>", actual.getExpiresAt());
        }
        return this;
    }
}