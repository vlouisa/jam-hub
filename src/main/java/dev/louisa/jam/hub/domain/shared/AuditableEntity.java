package dev.louisa.jam.hub.domain.shared;

import java.time.Instant;

public interface AuditableEntity {
    void setRecordCreationDateTime(Instant dateTime);
    void setRecordCreationUser(String userId);
    void setRecordModificationDateTime(Instant dateTime);
    void setRecordModificationUser(String userId);

    Instant getRecordCreationDateTime();
    String getRecordCreationUser();
    Instant getRecordModificationDateTime();
    String getRecordModificationUser();
}
