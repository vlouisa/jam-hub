package dev.louisa.jam.hub.domain.gig.persistence;

import dev.louisa.jam.hub.domain.gig.GigStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GigStatusConverter implements AttributeConverter<GigStatus, String> {

    @Override
    public String convertToDatabaseColumn(GigStatus gigStatus) {
        return gigStatus != null ? gigStatus.getCode() : null;
    }

    @Override
    public GigStatus convertToEntityAttribute(String dbData) {
        return dbData != null ? GigStatus.fromCode(dbData) : null;
    }
}