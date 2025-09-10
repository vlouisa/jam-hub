package dev.louisa.jam.hub.application.registration;

import dev.louisa.jam.hub.testsupport.BaseApplicationTest;
import dev.louisa.jam.hub.application.exceptions.ApplicationException;
import dev.louisa.jam.hub.domain.registration.UserRegistration;
import dev.louisa.jam.hub.domain.registration.persistence.UserRegistrationRepository;
import dev.louisa.jam.hub.testsupport.Factory.domain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

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
        when(userRegistrationRepository.findByOtp(registration.getOtp()))
                .thenReturn(Optional.of(registration));
        
        registrationApplicationService.verifyOtp(registration.getOtp());

        assertThat(registration.isVerified()).isTrue();
        verify(userRegistrationRepository).save(registration);
    }

    @Test
    void shouldNotVerifyRegistrationWhenAlreadyVerified() {
        registration = domain.aUserRegistration.createVerified();
        var verificationAt= registration.getVerifiedAt();
        
        when(userRegistrationRepository.findByOtp(registration.getOtp()))
                .thenReturn(Optional.of(registration));
        
        registrationApplicationService.verifyOtp(registration.getOtp());

        assertThat(registration.isVerified()).isTrue();
        assertThat(registration.getVerifiedAt()).isEqualTo(verificationAt);
        verify(userRegistrationRepository, times(0)).save(registration);
    }

    @Test
    void shouldThrowWhenRegistrationDoesNotExist() {
        UUID otp = UUID.randomUUID();
        when(userRegistrationRepository.findByOtp(otp))
                .thenReturn(Optional.empty());
        
        assertThatCode(() -> registrationApplicationService.verifyOtp(otp))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(ENTITY_NOT_FOUND.getMessage());
        
        verify(userRegistrationRepository, times(0)).save(any());
    
    }
    
}