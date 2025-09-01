package dev.louisa.jam.hub.domain.band;

import dev.louisa.jam.hub.shared.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "jhb_bands")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Band implements AuditableEntity {

    @EmbeddedId
    private BandId id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BandMember> members = new ArrayList<>();

    private Instant recordCreationDateTime;
    private String recordCreationUser;
    private Instant recordModificationDateTime;
    private String recordModificationUser;
    
    // Domain behavior
    public void addMember(BandMember member) {
        members.add(member);
        member.setBand(this);
    }

    public void removeMember(BandMember member) {
        members.remove(member);
        member.setBand(null);
    }
}
