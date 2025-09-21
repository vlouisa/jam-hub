package dev.louisa.jam.hub.testsupport.asserts;

import dev.louisa.jam.hub.application.auth.port.outbound.PasswordHasher;
import dev.louisa.jam.hub.domain.common.EmailAddress;
import dev.louisa.jam.hub.domain.user.User;
import dev.louisa.jam.hub.domain.user.UserId;
import org.assertj.core.api.AbstractAssert;

public class UserAssert extends AbstractAssert<UserAssert, User> {
    private PasswordHasher passwordHasher;
    
    private UserAssert(User actual) {
        super(actual, UserAssert.class);
    }

    public static UserAssert assertThatUser(User actual) {
        return new UserAssert(actual);
    }
    
    public UserAssert usingPasswordHasher(PasswordHasher passwordHasher) {
        this.passwordHasher = passwordHasher;
        return this;
    }

    public UserAssert hasId(UserId expectedId) {
        isNotNull();
        if (!actual.getId().equals(expectedId)) {
            failWithMessage("Expected id <%s> but was <%s>", expectedId, actual.getId());
        }
        return this;
    }

    public UserAssert hasEmail(EmailAddress expectedEmail) {
        isNotNull();
        if (!actual.getEmail().equals(expectedEmail)) {
            failWithMessage("Expected email <%s> but was <%s>", expectedEmail, actual.getEmail());
        }
        return this;
    }

    public UserAssert hasPassword(String expectedRawPassword) {
        isNotNull();
        
        if (passwordHasher == null) {
            throw new IllegalStateException("PasswordHasher must be set using usingPasswordHasher() before calling hasPassword()");
        }
        
        if (!passwordHasher.matches(expectedRawPassword, actual.getHashedPassword().value())) {
            failWithMessage("Expected password does not match actual password");
        }
        return this;
    }

    public UserAssert hasDisplayName(String expectedDisplayName) {
        isNotNull();
        if (!actual.getDisplayName().equals(expectedDisplayName)) {
            failWithMessage("Expected displayName <%s> but was <%s>", expectedDisplayName, actual.getDisplayName());
        }
        return this;
    }

    public UserAssert hasDisplayNameDerivedFromEmail() {
        isNotNull();
        if (!actual.getDisplayName().equals(actual.getEmail().email())) {
            failWithMessage("Expected displayName to be derived from email <%s> but was <%s>",
                    actual.getEmail().email(), actual.getDisplayName());
        }
        return this;
    }
}