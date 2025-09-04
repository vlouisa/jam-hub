package dev.louisa.jam.hub.application.registration;

import dev.louisa.jam.hub.application.BaseApplicationTest;
import dev.louisa.jam.hub.application.exceptions.ApplicationException;
import dev.louisa.jam.hub.domain.registration.UserRegistration;
import dev.louisa.jam.hub.domain.registration.UserRegistrationId;
import dev.louisa.jam.hub.domain.registration.persistence.UserRegistrationRepository;
import dev.louisa.jam.hub.testsupport.Factory.domain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static dev.louisa.jam.hub.application.exceptions.ApplicationError.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationApplicationServiceTest extends BaseApplicationTest {
    
    private UserRegistration registration;
    @Mock
    private UserRegistrationRepository userRegistrationRepository;
    
    
    private RegistrationApplicationService registrationApplicationService;

    @BeforeEach
    void setUp() {
        registrationApplicationService = new RegistrationApplicationService(userRegistrationRepository);
    }
    
    @Test
    void shouldVerifyRegistration() {
        registration = domain.aUserRegistration.create();
        when(userRegistrationRepository.findById(registration.getId()))
                .thenReturn(Optional.of(registration));
        
        registrationApplicationService.verifyOtp(registration.getId());

        assertThat(registration.isVerified()).isTrue();
        verify(userRegistrationRepository).save(registration);
    }

    @Test
    void shouldNotVerifyRegistrationWhenAlreadyVerified() {
        registration = domain.aUserRegistration.createVerified();
        var verificationAt= registration.getVerifiedAt();
        
        when(userRegistrationRepository.findById(registration.getId()))
                .thenReturn(Optional.of(registration));
        
        registrationApplicationService.verifyOtp(registration.getId());

        assertThat(registration.isVerified()).isTrue();
        assertThat(registration.getVerifiedAt()).isEqualTo(verificationAt);
        verify(userRegistrationRepository, times(0)).save(registration);
    }

    @Test
    void shouldThrowWhenRegistrationDoesNotExist() {
        UserRegistrationId registrationId = UserRegistrationId.generate();
        when(userRegistrationRepository.findById(registrationId))
                .thenReturn(Optional.empty());
        
        assertThatCode(() -> registrationApplicationService.verifyOtp(registrationId))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(ENTITY_NOT_FOUND.getMessage());
        
        verify(userRegistrationRepository, times(0)).save(any());
    
    }
    
}