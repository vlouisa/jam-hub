package dev.louisa.jam.hub.domain.band.persistence;

import dev.louisa.jam.hub.domain.band.BandRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BandRoleConverter implements AttributeConverter<BandRole, String> {

    @Override
    public String convertToDatabaseColumn(BandRole role) {
        return role != null ? role.getCode() : null;
    }

    @Override
    public BandRole convertToEntityAttribute(String dbData) {
        return dbData != null ? BandRole.fromCode(dbData) : null;
    }
}