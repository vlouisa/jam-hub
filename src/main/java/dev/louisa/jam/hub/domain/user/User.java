package dev.louisa.jam.hub.domain.user;

import dev.louisa.jam.hub.domain.common.AggregateRoot;
import dev.louisa.jam.hub.domain.common.EmailAddress;
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
            @AttributeOverride(name = "hash", column = @Column(name = "password")),
    })
    private Password password;

    public static User createNewUser(EmailAddress email, Password password) {
        return User.builder()
                .id(UserId.generate())
                .email(email)
                .password(password)
                .displayName(email.email())
                .build();
    }
}
