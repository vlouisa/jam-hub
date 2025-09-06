package dev.louisa.jam.hub.domain.shared;

import java.util.UUID;

public interface Id {
    UUID id();
    
    default String toValue(){
        return id().toString();
    }
}