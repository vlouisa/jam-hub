package dev.louisa.jam.hub.domain.user;

import dev.louisa.jam.hub.domain.common.EmailAddress;
import lombok.Builder;

@Builder
public record Credentials(EmailAddress emailAddress, Password password) 
{}
