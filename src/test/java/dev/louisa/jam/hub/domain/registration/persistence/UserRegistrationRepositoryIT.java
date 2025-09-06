package dev.louisa.jam.hub.domain.registration.persistence;

import dev.louisa.jam.hub.testsupport.BaseIntegrationIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.louisa.jam.hub.testsupport.Factory.domain.*;
import static org.assertj.core.api.Assertions.assertThat;

class UserRegistrationRepositoryIT extends BaseIntegrationIT {
    @Autowired
    private UserRegistrationRepository userRegistrationRepository;

    @Test
    void shouldSaveUserRegistration() {
        var registration = aUserRegistration.createExpired();
        
        userRegistrationRepository.save(registration);
        
        var retrieved = userRegistrationRepository.findById(registration.getId()).orElseThrow();
        assertThat(retrieved).isEqualTo(registration);
        assertThat(retrieved.getVerifiedAt()).isNull();
        assertThat(retrieved.getExpiredAt()).isNotNull();
        assertThat(retrieved.getRevokedAt()).isNull();
    }

    @Test
    void shouldRetrieveRegistrationById() {
        var registration = aUserRegistration
                .usingRepository(userRegistrationRepository)
                .create();
        
        var retrieved = userRegistrationRepository.findById(registration.getId()).orElseThrow();
        assertThat(retrieved).isEqualTo(registration);
        assertThat(retrieved.getVerifiedAt()).isNull();
        assertThat(retrieved.getExpiredAt()).isNull();
        assertThat(retrieved.getRevokedAt()).isNull();
    }
}