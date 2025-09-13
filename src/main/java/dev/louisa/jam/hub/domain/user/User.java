package dev.louisa.jam.hub.domain.user;

import dev.louisa.jam.hub.domain.common.AggregateRoot;
import dev.louisa.jam.hub.domain.common.EmailAddress;
import dev.louisa.jam.hub.domain.user.event.UserCreatedEvent;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@Table(name = "jhb_users")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class User extends AggregateRoot<UserId> {

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

    public static User createNewUser(EmailAddress email) {
        final User user = User.builder()
                .id(UserId.generate())
                .email(email)
                .displayName(email.email())
                .build();

        user.recordDomainEvent(
                UserCreatedEvent.builder()
                        .userId(user.getId())
                        .emailAddress(user.getEmail())
                        .build());

        return user;
    }
}
