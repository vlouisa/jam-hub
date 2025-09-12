package dev.louisa.jam.hub.domain.common;

import java.util.UUID;

public interface Id {
    UUID id();
    
    default String toValue(){
        return id().toString();
    }
}