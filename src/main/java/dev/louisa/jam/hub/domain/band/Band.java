package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.domain.user.UserId;
import dev.louisa.jam.hub.domain.common.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "jhb_bands")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Band implements AuditableEntity {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private BandId id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BandMember> members = new ArrayList<>();

    @CreationTimestamp
    private Instant recordCreationDateTime;
    private UUID recordCreationUser;
    @UpdateTimestamp
    private Instant recordModificationDateTime;
    private UUID recordModificationUser;
    
    // Domain behavior
    public void addMember(BandMember member) {
        members.add(member);
        member.setBand(this);
    }

    public void removeMember(BandMember member) {
        members.remove(member);
        member.setBand(null);
    }
    
    public boolean hasMember(UserId userId) {
        return members.stream()
                .anyMatch(m -> m.getUserId().equals(userId.id()));
    }
}
