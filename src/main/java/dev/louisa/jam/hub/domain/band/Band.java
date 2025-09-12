package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.domain.common.AggregateRoot;
import dev.louisa.jam.hub.domain.user.UserId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "jhb_bands")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder()
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Band extends AggregateRoot<BandId>  {

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BandMember> members = new ArrayList<>();
    
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
