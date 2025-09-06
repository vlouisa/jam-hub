package dev.louisa.jam.hub.domain.shared;

import java.time.Instant;
import java.util.UUID;

public interface AuditableEntity {
    void setRecordCreationDateTime(Instant dateTime);
    void setRecordCreationUser(UUID userId);
    void setRecordModificationDateTime(Instant dateTime);
    void setRecordModificationUser(UUID userId);

    Instant getRecordCreationDateTime();
    UUID getRecordCreationUser();
    Instant getRecordModificationDateTime();
    UUID getRecordModificationUser();
}
