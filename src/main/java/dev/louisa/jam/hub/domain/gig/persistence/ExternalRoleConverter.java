package dev.louisa.jam.hub.domain.gig.persistence;

import dev.louisa.jam.hub.domain.gig.ExternalRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ExternalRoleConverter implements AttributeConverter<ExternalRole, String> {

    @Override
    public String convertToDatabaseColumn(ExternalRole role) {
        return role != null ? role.getCode() : null;
    }

    @Override
    public ExternalRole convertToEntityAttribute(String dbData) {
        return dbData != null ? ExternalRole.fromCode(dbData) : null;
    }
}