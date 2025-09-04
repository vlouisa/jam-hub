package dev.louisa.jam.hub.domain.shared;


import dev.louisa.jam.hub.exceptions.JamHubException;

import java.util.UUID;

public interface Id {
    UUID id();
    
    default String toValue(){
        return id().toString();
    }
}